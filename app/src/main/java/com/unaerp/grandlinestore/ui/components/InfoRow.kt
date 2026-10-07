package com.unaerp.grandlinestore.ui.components

import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.unaerp.grandlinestore.databinding.ViewInfoRowBinding

/**
 * Preenche uma linha "rótulo: valor" (`view_info_row.xml`, reutilizada via `<include>`).
 * A linha inteira é escondida quando o valor opcional não existe.
 */
fun ViewInfoRowBinding.bind(@StringRes labelRes: Int, value: CharSequence?) {
    root.isVisible = !value.isNullOrBlank()
    tvLabel.setText(labelRes)
    tvValue.text = value
}
