package com.unaerp.grandlinestore.ui.catalog

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.databinding.ActivityMainBinding
import com.unaerp.grandlinestore.domain.model.Cart
import com.unaerp.grandlinestore.ui.common.PriceFormatter
import com.unaerp.grandlinestore.ui.common.applySystemBarsPadding

/**
 * Tela 1 — Catálogo. Hospeda o [ProductListFragment] e exibe o contador da sacola.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: CatalogViewModel by viewModels { CatalogViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.applySystemBarsPadding(applyTop = true)
        binding.btnCart.setOnClickListener { showCartSummary() }

        viewModel.cartQuantity.observe(this, ::renderCartBadge)
    }

    private fun renderCartBadge(quantity: Int) {
        binding.tvCartBadge.isVisible = quantity > 0
        binding.tvCartBadge.text = if (quantity > MAX_BADGE_VALUE) {
            getString(R.string.cart_badge_overflow, MAX_BADGE_VALUE)
        } else {
            quantity.toString()
        }
        binding.btnCart.contentDescription =
            resources.getQuantityString(R.plurals.cart_content_description, quantity, quantity)
    }

    private fun showCartSummary() {
        val cart = viewModel.cart.value ?: Cart()
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.cart_dialog_title)
            .setMessage(buildCartMessage(cart))
            .setPositiveButton(R.string.action_ok, null)
            .show()
    }

    private fun buildCartMessage(cart: Cart): String {
        if (cart.isEmpty) return getString(R.string.cart_dialog_empty)
        return buildString {
            cart.items.forEach { item ->
                appendLine(
                    getString(
                        R.string.cart_dialog_item,
                        item.quantity,
                        item.product.name,
                        PriceFormatter.format(item.subtotalInCents),
                    )
                )
            }
            appendLine()
            append(getString(R.string.cart_dialog_total, PriceFormatter.format(cart.totalInCents)))
        }
    }

    private companion object {
        const val MAX_BADGE_VALUE = 99
    }
}
