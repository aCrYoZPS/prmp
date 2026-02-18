package com.example.lab1

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ConverterFragment : Fragment(), KeyboardFragment.KeyboardListener {
    private lateinit var converter: Converter
    private lateinit var upperSpinner: Spinner
    private lateinit var lowerSpinner: Spinner
    private lateinit var upperAdapter: ArrayAdapter<UnitModel>
    private lateinit var lowerAdapter: ArrayAdapter<UnitModel>
    private lateinit var upperEditText: EditText
    private lateinit var lowerEditText: EditText
    private lateinit var upperButtonCopy: Button
    private lateinit var lowerButtonCopy: Button
    private var allUnitsByType: Map<MeasurementType, List<UnitModel>> = emptyMap()
    private var allUnits: List<UnitModel> = emptyList()

    private var focusedEditText: EditText? = null

    private var pendingLowerUnit: UnitModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)

        view.viewTreeObserver.addOnGlobalFocusChangeListener { _, newFocus ->
            if (newFocus is EditText) {
                focusedEditText = newFocus
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upperEditText = view.findViewById(R.id.editTextNumberUpper)
        lowerEditText = view.findViewById(R.id.editTextNumberLower)
        upperEditText.requestFocus()
        upperEditText.showSoftInputOnFocus = false
        lowerEditText.showSoftInputOnFocus = false
        upperEditText.setText("0")
        lowerEditText.setText("0")

        val textView = view.findViewById<TextView>(R.id.textViewArrow)
        textView.setOnClickListener {
            swapUnits()
        }

        converter = Converter()
        upperSpinner = view.findViewById(R.id.spinnerUpper)
        lowerSpinner = view.findViewById(R.id.spinnerLower)

        allUnits = converter.lengthUnits + converter.volumeUnits + converter.areaUnits
        allUnitsByType = mapOf(
            MeasurementType.Length to converter.lengthUnits,
            MeasurementType.Volume to converter.volumeUnits,
            MeasurementType.Area to converter.areaUnits
        )

        setupAdapters()
        setupListeners()

        upperEditText.onTextChanged { upperEditText, text ->
            if (text.isEmpty()) {
                upperEditText.setText("0")
                upperEditText.setSelection(1)
                lowerEditText.setText("0")
                lowerEditText.setSelection(1)
            } else {
                convert(upperEditText, text)
            }
        }

        lowerEditText.onTextChanged { lowerEditText, text ->
            if (text.isEmpty()) {
                upperEditText.setText("0")
                lowerEditText.setText("0")
            } else {
                convert(lowerEditText, text)
            }
        }

        upperButtonCopy = view.findViewById(R.id.buttonCopyUpper)
        upperButtonCopy.setOnClickListener {
            copyToClipboard(upperEditText.text.toString(), "Copied value from upper field")
        }

        lowerButtonCopy = view.findViewById(R.id.buttonCopyLower)
        lowerButtonCopy.setOnClickListener {
            copyToClipboard(lowerEditText.text.toString(), "Copied value from lower field")
        }
    }

    private fun copyToClipboard(value: String, message: String? = null) {
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied text", value)
        clipboard.setPrimaryClip(clip)

        if (message != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapters() {
        upperAdapter = createUnitAdapter(allUnits)
        upperSpinner.adapter = upperAdapter

        lowerAdapter = createUnitAdapter(allUnits)
        lowerSpinner.adapter = lowerAdapter
    }

    private fun swapUnits() {
        val upperUnit = upperSpinner.selectedItem as UnitModel
        val lowerUnit = lowerSpinner.selectedItem as UnitModel
        pendingLowerUnit = upperUnit
        upperSpinner.setSelectionByValue(lowerUnit)

        upperEditText.text = lowerEditText.text
    }

    fun Spinner.setSelectionByValue(value: UnitModel) {
        val castAdapter = adapter as ArrayAdapter<UnitModel>;
        val position = castAdapter.getPosition(value)
        if (position >= 0) {
            setSelection(position)
        }
    }

    private fun convert(sender: EditText, text: String) {
        val value = text.toDoubleOrNull() ?: return
        if (sender == upperEditText) {
            lowerEditText.setText(
                Utils.round(
                    converter.convert(
                        value,
                        upperSpinner.selectedItem as UnitModel,
                        lowerSpinner.selectedItem as UnitModel
                    ), 4
                ).toString()
            )
        } else {
            upperEditText.setText(
                Utils.round(
                    converter.convert(
                        value,
                        lowerSpinner.selectedItem as UnitModel,
                        upperSpinner.selectedItem as UnitModel
                    ), 4
                ).toString()
            )
        }
    }

    private fun createUnitAdapter(units: List<UnitModel>): ArrayAdapter<UnitModel> {
        return object :
            ArrayAdapter<UnitModel>(requireContext(), android.R.layout.simple_spinner_item, units) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.text = getItem(position)?.name
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.text = getItem(position)?.name
                return view
            }
        }.also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun setupListeners() {
        upperSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedUpper = upperAdapter.getItem(position) ?: return
                val type = selectedUpper.type
                val sameTypeUnits = allUnitsByType[type] ?: return

                val filteredUnits = sameTypeUnits.filter { it != selectedUpper }

                lowerAdapter = createUnitAdapter(filteredUnits)
                lowerSpinner.adapter = lowerAdapter

                if (pendingLowerUnit != null) {
                    val newIndex = filteredUnits.indexOf(pendingLowerUnit)

                    if (newIndex >= 0) {
                        lowerSpinner.setSelection(newIndex)
                    } else {
                        lowerSpinner.setSelection(0)
                    }

                    pendingLowerUnit = null

                } else {
                    lowerSpinner.setSelection(0)
                }

                convert(upperEditText, upperEditText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        lowerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convert(upperEditText, upperEditText.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun getFocusedEditText(): EditText {
        return if (focusedEditText == null) {
            requireView().findViewById(R.id.editTextNumberUpper)
        } else {
            focusedEditText!!
        }
    }

    override fun onInsert(key: String) {
        val focusedEdit = getFocusedEditText()
        val text = focusedEdit.text
        if (text.toString() == "0" && key != ".") {
            focusedEdit.setText(key)
        } else {
            focusedEdit.append(key)
        }

        focusedEdit.setSelection(text.length)
    }

    override fun onDelete() {
        val fte = getFocusedEditText()
        val text = fte.text
        if (text != null && text.isNotEmpty()) {
            fte.text!!.delete(text.length - 1, text.length)
        }
    }

    fun EditText.onTextChanged(action: (EditText, String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (this@onTextChanged == getFocusedEditText()) {
                    action(this@onTextChanged, s.toString())
                }
            }
        })
    }

}