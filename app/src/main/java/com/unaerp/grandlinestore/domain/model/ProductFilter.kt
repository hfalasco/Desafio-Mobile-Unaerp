package com.unaerp.grandlinestore.domain.model

/**
 * Critérios de filtragem do catálogo.
 *
 * @property category categoria selecionada; `null` significa "todas as categorias".
 * @property onlyInStock quando `true`, exibe apenas produtos com estoque disponível.
 */
data class ProductFilter(
    val category: ProductCategory? = null,
    val onlyInStock: Boolean = false,
) {

    fun matches(product: Product): Boolean {
        val matchesCategory = category == null || product.category == category
        val matchesStock = !onlyInStock || product.isAvailable
        return matchesCategory && matchesStock
    }
}

fun List<Product>.filterBy(filter: ProductFilter): List<Product> = filter(filter::matches)
