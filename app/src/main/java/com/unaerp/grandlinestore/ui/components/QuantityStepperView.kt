package com.unaerp.grandlinestore.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.databinding.ViewQuantityStepperBinding

/**
 * Componente reutilizável de seleção de quantidade ( − 1 + ).
 *
 * Infla `view_quantity_stepper.xml` (um `<merge>`) via ViewBinding e pode ser
 * configurado no XML pelos atributos `app:stepperMinValue`, `app:stepperMaxValue` e `app:stepperValue`.
 *
 * Alterações feitas pelo usuário disparam [onValueChangeListener]; alterações
 * programáticas em [value] apenas atualizam a interface (evitando loops com o ViewModel).
 */
class QuantityStepperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewQuantityStepperBinding.inflate(LayoutInflater.from(context), this)

    var onValueChangeListener: ((Int) -> Unit)? = null

    var minValue: Int = DEFAULT_MIN_VALUE
        set(newMin) {
            field = newMin
            value = value
        }

    var maxValue: Int = DEFAULT_MAX_VALUE
        set(newMax) {
            field = newMax
            value = value
        }

    var value: Int = DEFAULT_MIN_VALUE
        set(newValue) {
            field = newValue.coerceIn(minValue, maxOf(minValue, maxValue))
            render()
        }

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setBackgroundResource(R.drawable.bg_stepper)

        context.withStyledAttributes(attrs, R.styleable.QuantityStepperView, defStyleAttr) {
            minValue = getInt(R.styleable.QuantityStepperView_stepperMinValue, DEFAULT_MIN_VALUE)
            maxValue = getInt(R.styleable.QuantityStepperView_stepperMaxValue, DEFAULT_MAX_VALUE)
            value = getInt(R.styleable.QuantityStepperView_stepperValue, minValue)
        }

        binding.btnDecrease.setOnClickListener { changeValueBy(-1) }
        binding.btnIncrease.setOnClickListener { changeValueBy(+1) }
        render()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        render()
    }

    private fun changeValueBy(delta: Int) {
        val previous = value
        value = previous + delta
        if (value != previous) onValueChangeListener?.invoke(value)
    }

    private fun render() {
        binding.tvValue.text = value.toString()
        binding.btnDecrease.isEnabled = isEnabled && value > minValue
        binding.btnIncrease.isEnabled = isEnabled && value < maxValue
    }

    private companion object {
        const val DEFAULT_MIN_VALUE = 1
        const val DEFAULT_MAX_VALUE = 99
    }
}
