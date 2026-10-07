package com.unaerp.grandlinestore.domain.model

import com.unaerp.grandlinestore.testProduct
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class CartTest {

    @Test
    fun `sacola nova está vazia`() {
        val cart = Cart()

        assertTrue(cart.isEmpty)
        assertEquals(0, cart.totalQuantity)
        assertEquals(0L, cart.totalInCents)
    }

    @Test
    fun `adicionar o mesmo produto acumula a quantidade`() {
        val product = testProduct(stock = 10)

        val cart = Cart().add(product, 2).add(product, 3)

        assertEquals(1, cart.items.size)
        assertEquals(5, cart.quantityOf(product.id))
    }

    @Test
    fun `total usa o preço promocional`() {
        val onSale = testProduct(id = "a", priceInCents = 10_000, promotionalPriceInCents = 8_000)
        val regular = testProduct(id = "b", priceInCents = 5_000)

        val cart = Cart().add(onSale, 2).add(regular, 1)

        assertEquals(3, cart.totalQuantity)
        assertEquals(21_000L, cart.totalInCents)
    }

    @Test
    fun `quantidade é limitada ao estoque disponível`() {
        val product = testProduct(stock = 3)

        val cart = Cart().add(product, 2).add(product, 5)

        assertEquals(3, cart.quantityOf(product.id))
    }

    @Test
    fun `adicionar produto esgotado não altera a sacola`() {
        val original = Cart()

        val result = original.add(testProduct(stock = 0), 1)

        assertSame(original, result)
    }
}
