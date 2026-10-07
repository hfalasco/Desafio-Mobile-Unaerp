package com.unaerp.grandlinestore.ui.common

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

/** Formata valores em centavos como moeda brasileira (ex.: 34990 -> "R$ 349,90"). */
object PriceFormatter {

    private val brazilianLocale: Locale = Locale.forLanguageTag("pt-BR")

    fun format(cents: Long): String =
        // NumberFormat não é thread-safe; criar a instância por chamada evita estado compartilhado.
        NumberFormat.getCurrencyInstance(brazilianLocale).format(BigDecimal.valueOf(cents, 2))
}
