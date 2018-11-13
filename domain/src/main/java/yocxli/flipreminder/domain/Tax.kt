package yocxli.flipreminder.domain

sealed class Tax(val ratio: Float) {
    fun calculate(price: Int) = price * (1 + ratio)
}

class JapaneseConsumptionTax : Tax(0.08F)