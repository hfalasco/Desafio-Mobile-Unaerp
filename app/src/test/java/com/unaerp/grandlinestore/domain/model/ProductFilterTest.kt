package com.unaerp.grandlinestore.domain.model

import com.unaerp.grandlinestore.testProduct
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductFilterTest {

    private val figure = testProduct(id = "figure", category = ProductCategory.FIGURE, stock = 5)
    private val soldOutFigure = testProduct(id = "figure-out", category = ProductCategory.FIGURE, stock = 0)
    private val manga = testProduct(id = "manga", category = ProductCategory.MANGA, stock = 2)
    private val products = listOf(figure, soldOutFigure, manga)

    @Test
    fun `filtro padrão retorna todos os produtos`() {
        assertEquals(products, products.filterBy(ProductFilter()))
    }

    @Test
    fun `filtro por categoria retorna apenas a categoria escolhida`() {
        val result = products.filterBy(ProductFilter(category = ProductCategory.FIGURE))

        assertEquals(listOf(figure, soldOutFigure), result)
    }

    @Test
    fun `filtro de estoque remove produtos esgotados`() {
        val result = products.filterBy(ProductFilter(onlyInStock = true))

        assertEquals(listOf(figure, manga), result)
    }

    @Test
    fun `filtros combinados podem resultar em lista vazia`() {
        val result = products.filterBy(
            ProductFilter(category = ProductCategory.CARD_GAME, onlyInStock = true)
        )

        assertEquals(emptyList<Product>(), result)
    }
}
