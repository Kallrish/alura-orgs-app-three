package br.com.alura.orgs_two.extensions

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun BigDecimal.formatBrazilCurrency(): String? {
    val currencyFormatter = NumberFormat
        .getCurrencyInstance(Locale("pt", "br"))
    return currencyFormatter.format(this)
}