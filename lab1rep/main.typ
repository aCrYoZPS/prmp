#import "lib/stp2024.typ"
#include "title.typ"

#show: stp2024.template

#stp2024.full_outline()
#pagebreak()

= Формулировка задачи
В рамках данной лабораторной работы необходимо разработать мобильное приложение-конвертер единиц
измерения или валют. Требования включают в себя:
#enum(
  [Приложение должно содержать не менее трёх различных категорий единиц.],
  [Каждая из категорий единиц должна содержать не менее трёх различных единиц.],
  [
    Приложение должно содержать цифровую клавиатуру для ввода значений, должна
    отсутствовать возможность вводить значения со стандартной клавиатуры.
  ],
  [
    Приложение должно содержать две версии: premium-версию, функционал которой дополнительно
    включает кнопку, которая будет менять местами исходные и преобразованные значения
    (и единицы измерения), и наоборот, а также кнопку для копирования текущего значения в
    буфер обмена рядом с каждым полем ввода данных.
  ],
)

Также необходимо предоставить диаграмму объектов.

= Выполнение работы
== Цифровая клавиатура
Изначально была создан фрагмент, представляющий собой клавиатуру из 12 кнопок, 10 из которых
циферные, одна кнопка десятичного разделителя (точки) и кнопка удаления последнего символа
(backspace). Макет данного фрагмента представлен на рисунке @keyboard_fragment.

#figure(
  caption: [Фрагмент цифровой клавиатуры],
  image("./img/keyboard_fragment.png"),
) <keyboard_fragment>

Данный макет был создан с помощью сетки, в которой расположены кнопки. Фрагмент определяет
интерфейс KeyboardListener с двумя методами onInsert(key: String) и onDelete(), с помощью
которых и происходит передача информации о нажатых кнопках из обработчика в фрагменте в
целевой класс. Исходный код данного фрагмента представлен в листинге @keyboard_fragment_code.

== Фрагмент с данными
Далее был создан фрагмент с данными, состоящий из двух элементов EditText для отображения
значений исходной и конвертированной величин, двух элементов Spinner для выбора величин и
TextView со стрелкой для наглядности конвертации. Макет данного фрагмента для build flavour
standard показан на рисунке @standard_converter.

#figure(
  caption: [Фрагмент с данными в конфигурации standard],
  image("./img/standard_converter.png"),
) <standard_converter>

Для build flavour premium нажатие на TextView меняет местами величины и единицы измерения,
а также добавлены кнопки копирования значений обеих величин. Макет данного фрагмента для
build flavour premium показан на рисунке @premium_converter.

#figure(
  caption: [Фрагмент с данными в конфигурации premium],
  image("./img/premium_converter.png"),
) <premium_converter>

В качестве категорий были выбраны длина, площадь и объём. Категория длина включает в себя
такие единицы измерения как метр, фут и ярд. Категория площадь включает в себя такие
единицы как квадратный метр, гектар и акр. Категория объём включает в себя такие единицы
как кубический метр, литр и галлон (США). Выбор единиц измерения представлен на рисунке
@units_choice

#figure(
  caption: [Содержимое Spinner со всеми единицами измерения],
  image("./img/units_choice.jpg", height: 50%),
) <units_choice>

Исходный код данного фрагмента для bulid flavour premium представлен в листинге
@converter_fragment_code. Исходный код для build flavour standard практически идентичен,
поэтому представлен не будет.

Диаграмма объектов представлена на рисунке @object_diagram.

#figure(
  caption: [Диаграмма объектов],
  image("./img/units_choice.jpg", height: 50%),
) <object_diagram>

Таким образом были выполнены все требования к разработанному программному продукту.

== Подпись приложения
Одним из общих требований для всего курса лабораторных работ является подпись APK-файлов
собственным сертификатом. Для этого с помощью Android Studio был создан keystore-файл, в
который была внесена личная информация. Далее был дополнен файл сборки build.gradle.kts,
в который был добавлен SigningConfig для автоматического подписания собранных APK-файлов
личным сертификатом. На телефон для тестирования было установлено приложение Apk Analyzer,
результат его работы показан на рисунке @certificate_proof.
#figure(
  caption: [Сертификат, найденный внутри приложения с помощью Apk Analyzer],
  image("./img/certificate_proof.jpg", height: 50%),
) <certificate_proof>

Сертификат нужен для уверенности в целостности кода, то есть неизменённости исходного кода
кем-либо кроме подписавшего, а также для обновления приложения, так как для установки
обновления поверх старой версии необходимо, чтобы новая была подписана тем же сертификатом.


#pagebreak()
#stp2024.heading_unnumbered[Заключение]
В ходе выполнения лабораторной работы было разработано мобильное приложение конвертер единиц
измерения.  Были рассмотрены базовые сущности при разработке под Android, такие как Activity
и Fragment.

В ходе выполнения лабораторной работы было успешно разработано мобильное приложение-конвертер
единиц измерения, полностью соответствующее поставленным требованиям. Приложение включает три
категории единиц (длина, объём, валюта), каждая из которых содержит не менее трёх единиц
измерения. Реализована цифровая клавиатура для ввода значений, исключающая использование
стандартной клавиатуры устройства. Созданы две версии приложения: standard и premium, с
дополнительным функционалом для premium-версии — кнопкой взаимного обмена значениями и единицами,
а также кнопками копирования в буфер обмена.

Приложение может быть расширено добавлением офлайн-режима работы с кэшированием курсов валют,
поддержкой дополнительных категорий (температура, масса, скорость), а также интеграцией с
облачными сервисами для синхронизации пользовательских настроек.

Разработанный программный продукт был протестирован на эмуляторе в среде разработки Android
Studio а также на реальном телефоне под управлением ОС Android.


В заключение можно отметить, что выполнение данной лабораторной работы позволило не только
изучить основные элементы разработки под ОС Android, но и получить практический опыт их
применения. Данные навыки могут быть успешно использованы для решения задач в области мобильной
разработки. Полученные знания и навыки станут основой для дальнейшего изучения платформы Android,
языка Kotlin и их применения в профессиональной деятельности.

#stp2024.appendix(type: [обязательное], title: [Листинг программного кода])[

  #stp2024.listing[Исходный код фрагмента цифровой клавиатуры][
    ```
    class KeyboardFragment : Fragment() {
        interface KeyboardListener {
            fun onInsert(key: String)
            fun onDelete()
        }

        private var listener: KeyboardListener? = null

        fun setListener(listener: KeyboardListener) {
            this.listener = listener
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_keyboard, container, false)
            val gridLayout =
                view.findViewById<GridLayout>(R.id.keyboard_grid)

            for (child in gridLayout.children) {
                if (child is Button) {
                    child.setOnClickListener { handleButtonClick(child) }
                }
            }

            return view
        }

        private fun handleButtonClick(button: Button) {
            val tag = button.tag.toString()

            if (tag == "backspace") {
                listener?.onDelete()
            } else {
                listener?.onInsert(tag)
            }
        }
    }
    ```
  ] <keyboard_fragment_code>


  #stp2024.listing[Исходный код фрагмента цифровой клавиатуры][
    ```
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
    ```
  ] <converter_fragment_code>
]
