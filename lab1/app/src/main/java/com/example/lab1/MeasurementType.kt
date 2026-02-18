package com.example.lab1

sealed class MeasurementType {
    object Length : MeasurementType()
    object Volume : MeasurementType()
    object Area : MeasurementType()
}