package com.unaerp.grandlinestore.ui.catalog

import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.model.ProductFilter

/** Estado imutável da tela de catálogo, renderizado pelo [ProductListFragment]. */
data class CatalogUiState(
    val products: List<Product>,
    val filter: ProductFilter,
) {
    val isEmpty: Boolean
        get() = products.isEmpty()
}
