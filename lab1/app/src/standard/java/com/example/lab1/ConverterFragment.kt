package com.example.lab1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment

class ConverterFragment : Fragment(), KeyboardFragment.KeyboardListener {
    private lateinit var converter: Converter
    private lateinit var upperSpinner: Spinner
    private lateinit var lowerSpinner: Spinner
    private lateinit var upperAdapter: ArrayAdapter<UnitModel>
    private lateinit var lowerAdapter: ArrayAdapter<UnitModel>
    private lateinit var upperEditText: EditText
    private lateinit var lowerEditText: EditText
    private var allUnitsByType: Map<MeasurementType, List<UnitModel>> = emptyMap()
    private var allUnits: List<UnitModel> = emptyList()

    private var focusedEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
    }

    private fun setupAdapters() {
        upperAdapter = createUnitAdapter(allUnits)
        upperSpinner.adapter = upperAdapter

        lowerAdapter = createUnitAdapter(allUnits)
        lowerSpinner.adapter = lowerAdapter
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
                lowerSpinner.setSelection(0)
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

        focusedEdit.setSelection(focusedEdit.text.length)
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