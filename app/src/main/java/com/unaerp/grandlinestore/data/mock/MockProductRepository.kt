package com.unaerp.grandlinestore.data.mock

import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.repository.ProductRepository

/** Implementação em memória de [ProductRepository] baseada em dados simulados. */
class MockProductRepository(
    private val products: List<Product> = MockProductDataSource.products,
) : ProductRepository {

    private val productsById: Map<String, Product> = products.associateBy(Product::id)

    override fun getProducts(): List<Product> = products

    override fun getProductById(id: String): Product? = productsById[id]
}
