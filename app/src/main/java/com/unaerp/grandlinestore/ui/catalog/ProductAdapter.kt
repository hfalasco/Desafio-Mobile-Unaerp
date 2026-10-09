package com.unaerp.grandlinestore.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.databinding.ItemProductBinding
import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.ui.common.PriceFormatter
import com.unaerp.grandlinestore.ui.common.bindProductImage
import com.unaerp.grandlinestore.ui.common.colorRes
import com.unaerp.grandlinestore.ui.common.discountLabel
import com.unaerp.grandlinestore.ui.common.labelRes
import com.unaerp.grandlinestore.ui.common.setStrikeThrough
import com.unaerp.grandlinestore.ui.common.setTextOrGone
import com.unaerp.grandlinestore.ui.common.stockLabel

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, onProductClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        private val binding: ItemProductBinding,
        onProductClick: (Product) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundProduct: Product? = null

        init {
            binding.root.setOnClickListener { boundProduct?.let(onProductClick) }
        }

        fun bind(product: Product) {
            boundProduct = product
            val context = binding.root.context
            val resources = context.resources

            binding.ivProductIcon.bindProductImage(
                product,
                iconSizePx = resources.getDimensionPixelSize(R.dimen.product_thumbnail_icon_size),
                photoScaleType = ImageView.ScaleType.CENTER_CROP,
            )
            binding.tvCategory.setText(product.category.labelRes)
            binding.tvName.text = product.name
            binding.tvCharacter.setTextOrGone(
                product.character?.let { resources.getString(R.string.product_character, it) }
            )

            binding.tvDiscountBadge.setTextOrGone(product.discountLabel(resources))
            binding.tvOriginalPrice.setStrikeThrough(true)
            binding.tvOriginalPrice.setTextOrGone(
                if (product.isOnSale) PriceFormatter.format(product.priceInCents) else null
            )
            binding.tvPrice.text = PriceFormatter.format(product.finalPriceInCents)

            binding.tvStockStatus.text = product.stockLabel(resources)
            binding.tvStockStatus.setTextColor(
                ContextCompat.getColor(context, product.stockStatus.colorRes)
            )
            binding.root.alpha = if (product.isAvailable) 1f else SOLD_OUT_ALPHA
        }

        private companion object {
            const val SOLD_OUT_ALPHA = 0.65f
        }
    }

    private object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }
}
