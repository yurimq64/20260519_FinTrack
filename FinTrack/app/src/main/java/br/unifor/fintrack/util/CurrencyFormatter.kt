package br.unifor.fintrack.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    private val brazilianLocale = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()

    private val currencyFormat: NumberFormat = NumberFormat
        .getCurrencyInstance(brazilianLocale)

    fun formatCents(amountInCents: Long): String {
        return currencyFormat.format(amountInCents / 100.0)
    }

    fun formatCentsWithSign(amountInCents: Long, isIncome: Boolean): String {
        val formatted = formatCents(kotlin.math.abs(amountInCents))
        return if (isIncome) "+$formatted" else "-$formatted"
    }

}