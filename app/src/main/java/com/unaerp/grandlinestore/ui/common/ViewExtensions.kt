package com.unaerp.grandlinestore.ui.common

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding

/** Exibe o texto quando presente; esconde a View quando o valor opcional é nulo ou vazio. */
fun TextView.setTextOrGone(value: CharSequence?) {
    text = value
    isVisible = !value.isNullOrBlank()
}

fun TextView.setStrikeThrough(enabled: Boolean) {
    paintFlags = if (enabled) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

/**
 * Soma as barras do sistema ao padding original da View (edge-to-edge, obrigatório
 * a partir do Android 15 com targetSdk 35).
 */
fun View.applySystemBarsPadding(applyTop: Boolean = false, applyBottom: Boolean = false) {
    val initialTop = paddingTop
    val initialBottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(
            top = initialTop + if (applyTop) bars.top else 0,
            bottom = initialBottom + if (applyBottom) bars.bottom else 0,
        )
        insets
    }
}
