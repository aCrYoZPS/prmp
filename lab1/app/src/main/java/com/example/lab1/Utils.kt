package com.example.lab1

import kotlin.math.pow
import kotlin.math.roundToInt

class Utils {
    companion object {
        fun round(value: Double, n: Int): Double {
            val factor = 10.0.pow(n)
            return (value * factor).roundToInt() / factor
        }
    }
}