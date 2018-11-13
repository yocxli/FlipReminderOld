package yocxli.flipreminder.domain

data class Card(val product: Product, val isFront: Boolean = true, val price: Price? = null)