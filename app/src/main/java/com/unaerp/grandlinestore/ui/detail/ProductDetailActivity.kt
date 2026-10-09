package com.unaerp.grandlinestore.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.databinding.ActivityProductDetailBinding
import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.ui.common.PriceFormatter
import com.unaerp.grandlinestore.ui.common.applySystemBarsPadding
import com.unaerp.grandlinestore.ui.common.bindProductImage
import com.unaerp.grandlinestore.ui.common.discountLabel
import com.unaerp.grandlinestore.ui.common.labelRes
import com.unaerp.grandlinestore.ui.common.setStrikeThrough
import com.unaerp.grandlinestore.ui.common.setTextOrGone
import com.unaerp.grandlinestore.ui.common.stockLabel
import com.unaerp.grandlinestore.ui.components.bind

/**
 * Tela 2 — Detalhe do produto em formato de cartaz "WANTED".
 *
 * Recebe o id do produto pela Intent explícita (ver [OpenProductDetailContract]) e
 * devolve um [AddToCartResult] quando o usuário confirma a compra.
 */
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    private val viewModel: ProductDetailViewModel by viewModels { ProductDetailViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)

        val product = viewModel.product
        if (product == null) {
            Toast.makeText(this, R.string.detail_product_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appBar.applySystemBarsPadding(applyTop = true)
        binding.bottomBar.applySystemBarsPadding(applyBottom = true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        bindPoster(product)
        bindDetails(product)
        setupPurchaseControls(product)
        observeViewModel()
    }

    private fun bindPoster(product: Product) {
        binding.ivProductIcon.bindProductImage(
            product,
            iconSizePx = resources.getDimensionPixelSize(R.dimen.poster_icon_size),
            photoScaleType = ImageView.ScaleType.FIT_CENTER,
        )
        binding.ivProductIcon.contentDescription = product.name
        binding.tvDiscountBadge.setTextOrGone(product.discountLabel(resources))
        binding.tvName.text = product.name
        binding.tvOriginalPrice.setStrikeThrough(true)
        binding.tvOriginalPrice.setTextOrGone(
            if (product.isOnSale) PriceFormatter.format(product.priceInCents) else null
        )
        binding.tvPrice.text = PriceFormatter.format(product.finalPriceInCents)
    }

    private fun bindDetails(product: Product) {
        binding.rowCategory.bind(R.string.detail_label_category, getString(product.category.labelRes))
        binding.rowCharacter.bind(R.string.detail_label_character, product.character)
        binding.rowArc.bind(R.string.detail_label_arc, product.arc)
        binding.rowRating.bind(
            R.string.detail_label_rating,
            product.rating?.let { getString(R.string.detail_rating_value, it) },
        )
        binding.rowStock.bind(R.string.detail_label_stock, product.stockLabel(resources))
        binding.tvDescription.text = product.description ?: getString(R.string.detail_no_description)
    }

    private fun setupPurchaseControls(product: Product) {
        val canPurchase = product.isAvailable

        binding.quantityStepper.apply {
            minValue = ProductDetailViewModel.MIN_QUANTITY
            maxValue = viewModel.maxQuantity
            isEnabled = canPurchase
            onValueChangeListener = viewModel::onQuantityChanged
        }

        binding.btnAddToCart.isEnabled = canPurchase
        binding.btnAddToCart.setText(
            if (canPurchase) R.string.detail_add_to_cart else R.string.stock_sold_out
        )
        binding.btnAddToCart.setOnClickListener { confirmAddToCart() }
    }

    private fun observeViewModel() {
        viewModel.quantity.observe(this) { quantity ->
            binding.quantityStepper.value = quantity
        }
        viewModel.totalInCents.observe(this) { total ->
            binding.tvTotalPrice.text = PriceFormatter.format(total)
        }
    }

    private fun confirmAddToCart() {
        val result = viewModel.buildAddToCartResult() ?: return
        setResult(RESULT_OK, OpenProductDetailContract.createResultIntent(result))
        finish()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "com.unaerp.grandlinestore.extra.PRODUCT_ID"
    }
}
