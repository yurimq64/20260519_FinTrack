package br.unifor.fintrack.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {

    private val brazilianLocale = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", brazilianLocale)

    private val absoluteDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", brazilianLocale)

    fun formatRelative(dateTime: LocalDateTime): String {
        val today = LocalDate.now()
        val transactionDate = dateTime.toLocalDate()
        val time = dateTime.format(timeFormatter)

        return when(transactionDate) {
            today -> "Hoje, $time"
            today.minusDays(1) -> "Ontem, $time"
            else -> dateTime.format(absoluteDateFormatter)
        }
    }
}