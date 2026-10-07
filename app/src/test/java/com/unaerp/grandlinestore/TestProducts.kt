package com.unaerp.grandlinestore

import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.model.ProductCategory

/** Construtor de produtos para testes, com valores padrão válidos. */
fun testProduct(
    id: String = "product-1",
    category: ProductCategory = ProductCategory.FIGURE,
    priceInCents: Long = 10_000,
    stock: Int = 10,
    promotionalPriceInCents: Long? = null,
) = Product(
    id = id,
    name = "Produto $id",
    category = category,
    priceInCents = priceInCents,
    stock = stock,
    promotionalPriceInCents = promotionalPriceInCents,
)
