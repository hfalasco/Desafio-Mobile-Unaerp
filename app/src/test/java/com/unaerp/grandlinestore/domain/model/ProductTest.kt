package com.unaerp.grandlinestore.domain.model

import com.unaerp.grandlinestore.testProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductTest {

    @Test
    fun `preço final é o original quando não há promoção`() {
        val product = testProduct(priceInCents = 10_000)

        assertFalse(product.isOnSale)
        assertEquals(10_000L, product.finalPriceInCents)
        assertNull(product.discountPercentage)
    }

    @Test
    fun `preço final é o promocional quando existe promoção`() {
        val product = testProduct(priceInCents = 10_000, promotionalPriceInCents = 7_500)

        assertTrue(product.isOnSale)
        assertEquals(7_500L, product.finalPriceInCents)
        assertEquals(25, product.discountPercentage)
    }

    @Test
    fun `status de estoque reflete a quantidade disponível`() {
        assertEquals(StockStatus.SOLD_OUT, testProduct(stock = 0).stockStatus)
        assertEquals(StockStatus.LOW, testProduct(stock = Product.LOW_STOCK_THRESHOLD).stockStatus)
        assertEquals(StockStatus.AVAILABLE, testProduct(stock = Product.LOW_STOCK_THRESHOLD + 1).stockStatus)
        assertFalse(testProduct(stock = 0).isAvailable)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `preço promocional maior que o original é rejeitado`() {
        testProduct(priceInCents = 10_000, promotionalPriceInCents = 12_000)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `estoque negativo é rejeitado`() {
        testProduct(stock = -1)
    }
}
