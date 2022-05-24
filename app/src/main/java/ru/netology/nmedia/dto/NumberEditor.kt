package ru.netology.nmedia.dto

import java.math.RoundingMode
import java.text.DecimalFormat

object NumberEditor {

    private val formatThousands = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.DOWN
    }

    fun numberEditing(number: Long): String =
        when (number) {
            in 1000..1099 -> "${number / 1000}K"
            in 1100..9999 -> "${formatThousands.format(number.toDouble() / 1000)}K"
            in 10_000..999_999 -> "${(number / 1000)}K"
            in 1_000_000..1_099_999 -> "${number / 1_000_000}M"
            in 1_100_000..999_999_999 -> "${formatThousands.format(number.toDouble() / 1_000_000)}M"
            else -> number.toString()
        }
}