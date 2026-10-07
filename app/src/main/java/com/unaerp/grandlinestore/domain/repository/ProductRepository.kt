package com.unaerp.grandlinestore.domain.repository

import com.unaerp.grandlinestore.domain.model.Product

/**
 * Contrato de acesso aos produtos.
 *
 * A UI depende apenas desta abstração; nesta etapa a implementação usa dados
 * simulados, mas pode ser trocada por uma API ou banco de dados sem alterar
 * ViewModels e telas.
 */
interface ProductRepository {

    fun getProducts(): List<Product>

    fun getProductById(id: String): Product?
}
