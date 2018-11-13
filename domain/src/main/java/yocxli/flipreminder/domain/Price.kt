package yocxli.flipreminder.domain

data class Price(private val price: Int, val tax: Tax? = null) {
    val taxExclusivePrice = price
    val taxInclusivePrice = tax?.calculate(price) ?: price
}