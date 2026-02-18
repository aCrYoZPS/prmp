package com.example.lab1

class Converter {
    val lengthUnits = listOf(
        UnitModel("Meters", 1.0, MeasurementType.Length),
        UnitModel("Yards", 0.9144, MeasurementType.Length),
        UnitModel("Feet", 0.3048, MeasurementType.Length)
    )

    val volumeUnits = listOf(
        UnitModel("Cubic Meters", 1.0, MeasurementType.Volume),
        UnitModel("Liters", 0.001, MeasurementType.Volume),
        UnitModel("Gallons (US)", 0.00379, MeasurementType.Volume),
    )

    val areaUnits = listOf(
        UnitModel("Square Meters", 1.0, MeasurementType.Area),
        UnitModel("Hectares", 10_000.0, MeasurementType.Area),
        UnitModel("Acres", 4_046.86, MeasurementType.Area),
    )

    fun convert(value: Double, from: UnitModel, to: UnitModel): Double {
        val baseValue = value * from.factorToBase;
        return baseValue / to.factorToBase;
    }
}