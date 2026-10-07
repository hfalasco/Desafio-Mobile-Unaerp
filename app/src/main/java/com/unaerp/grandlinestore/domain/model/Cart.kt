package com.unaerp.grandlinestore.domain.model

/** Linha da sacola: um produto e a quantidade escolhida. */
data class CartItem(
    val product: Product,
    val quantity: Int,
) {
    init {
        require(quantity > 0) { "A quantidade de um item na sacola deve ser positiva." }
    }

    val subtotalInCents: Long
        get() = product.finalPriceInCents * quantity
}

/**
 * Sacola de compras imutável: toda alteração devolve uma nova instância,
 * o que simplifica a publicação de estado pelo ViewModel.
 */
data class Cart(
    val items: List<CartItem> = emptyList(),
) {

    val totalQuantity: Int
        get() = items.sumOf { it.quantity }

    val totalInCents: Long
        get() = items.sumOf { it.subtotalInCents }

    val isEmpty: Boolean
        get() = items.isEmpty()

    fun quantityOf(productId: String): Int =
        items.firstOrNull { it.product.id == productId }?.quantity ?: 0

    /**
     * Adiciona [quantity] unidades de [product], acumulando com o que já está na sacola
     * e respeitando o limite de estoque. Retorna a própria instância quando nada muda
     * (produto esgotado ou limite já atingido).
     */
    fun add(product: Product, quantity: Int): Cart {
        require(quantity > 0) { "A quantidade adicionada deve ser positiva." }

        val currentQuantity = quantityOf(product.id)
        val newQuantity = (currentQuantity + quantity).coerceAtMost(product.stock)
        if (newQuantity <= currentQuantity) return this

        val updatedItem = CartItem(product, newQuantity)
        val updatedItems = if (currentQuantity == 0) {
            items + updatedItem
        } else {
            items.map { item -> if (item.product.id == product.id) updatedItem else item }
        }
        return copy(items = updatedItems)
    }
}
