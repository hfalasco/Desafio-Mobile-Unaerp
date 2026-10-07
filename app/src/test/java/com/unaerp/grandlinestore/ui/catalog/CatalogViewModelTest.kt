package com.unaerp.grandlinestore.ui.catalog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.unaerp.grandlinestore.data.mock.MockProductRepository
import com.unaerp.grandlinestore.domain.model.ProductCategory
import com.unaerp.grandlinestore.testProduct
import com.unaerp.grandlinestore.ui.detail.AddToCartResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CatalogViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val figure = testProduct(id = "figure", category = ProductCategory.FIGURE, stock = 2)
    private val manga = testProduct(id = "manga", category = ProductCategory.MANGA, stock = 0)
    private val viewModel = CatalogViewModel(MockProductRepository(listOf(figure, manga)))

    @Test
    fun `estado inicial exibe todos os produtos`() {
        val state = viewModel.uiState.value!!

        assertEquals(listOf(figure, manga), state.products)
        assertEquals(null, state.filter.category)
    }

    @Test
    fun `selecionar categoria filtra a lista`() {
        viewModel.onCategorySelected(ProductCategory.MANGA)

        assertEquals(listOf(manga), viewModel.uiState.value!!.products)
    }

    @Test
    fun `filtro de estoque pode deixar a lista vazia`() {
        viewModel.onCategorySelected(ProductCategory.MANGA)
        viewModel.onOnlyInStockChanged(true)

        assertTrue(viewModel.uiState.value!!.isEmpty)
    }

    @Test
    fun `resultado da tela de detalhe é adicionado à sacola`() {
        val added = viewModel.addToCart(AddToCartResult("figure", "Figure", quantity = 2))

        assertTrue(added)
        assertEquals(2, viewModel.cart.value!!.quantityOf("figure"))
    }

    @Test
    fun `não adiciona além do estoque nem produto inexistente`() {
        viewModel.addToCart(AddToCartResult("figure", "Figure", quantity = 2))

        assertFalse(viewModel.addToCart(AddToCartResult("figure", "Figure", quantity = 1)))
        assertFalse(viewModel.addToCart(AddToCartResult("unknown", "???", quantity = 1)))
        assertEquals(2, viewModel.cart.value!!.totalQuantity)
    }
}
