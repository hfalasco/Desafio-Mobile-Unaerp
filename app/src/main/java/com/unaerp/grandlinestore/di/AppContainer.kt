package com.unaerp.grandlinestore.di

import com.unaerp.grandlinestore.data.mock.MockProductRepository
import com.unaerp.grandlinestore.domain.repository.ProductRepository

/**
 * Injeção de dependências manual: centraliza a criação das dependências da app
 * para que ViewModels as recebam pelo construtor (facilitando testes).
 */
class AppContainer {

    val productRepository: ProductRepository by lazy { MockProductRepository() }
}
