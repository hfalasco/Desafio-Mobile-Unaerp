package com.unaerp.grandlinestore.domain.model

/**
 * Produto do catálogo (modelo imutável).
 *
 * Valores monetários são armazenados em centavos ([Long]) para evitar erros de
 * arredondamento de ponto flutuante.
 *
 * Campos opcionais (nullable) representam informações que nem todo produto possui:
 * @property promotionalPriceInCents preço promocional; `null` quando não há promoção.
 * @property description descrição detalhada; `null` quando ainda não cadastrada.
 * @property character personagem de One Piece relacionado; `null` para itens genéricos.
 * @property arc arco da história relacionado; `null` quando não se aplica.
 * @property rating avaliação média de 0 a 5; `null` quando o produto ainda não foi avaliado.
 */
data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val priceInCents: Long,
    val stock: Int,
    val promotionalPriceInCents: Long? = null,
    val description: String? = null,
    val character: String? = null,
    val arc: String? = null,
    val rating: Double? = null,
) {

    init {
        require(id.isNotBlank()) { "O id do produto não pode ser vazio." }
        require(name.isNotBlank()) { "O nome do produto não pode ser vazio." }
        require(priceInCents > 0) { "O preço deve ser positivo." }
        require(stock >= 0) { "O estoque não pode ser negativo." }
        require(promotionalPriceInCents == null || promotionalPriceInCents in 1L until priceInCents) {
            "O preço promocional deve ser positivo e menor que o preço original."
        }
        require(rating == null || rating in MIN_RATING..MAX_RATING) {
            "A avaliação deve estar entre $MIN_RATING e $MAX_RATING."
        }
    }

    val isOnSale: Boolean
        get() = promotionalPriceInCents != null

    /** Preço efetivamente cobrado: o promocional, se existir, ou o original. */
    val finalPriceInCents: Long
        get() = promotionalPriceInCents ?: priceInCents

    /** Percentual de desconto arredondado para baixo; `null` quando não há promoção. */
    val discountPercentage: Int?
        get() = promotionalPriceInCents?.let { promo ->
            ((priceInCents - promo) * 100 / priceInCents).toInt()
        }

    val isAvailable: Boolean
        get() = stock > 0

    val stockStatus: StockStatus
        get() = when {
            stock == 0 -> StockStatus.SOLD_OUT
            stock <= LOW_STOCK_THRESHOLD -> StockStatus.LOW
            else -> StockStatus.AVAILABLE
        }

    companion object {
        const val LOW_STOCK_THRESHOLD = 3
        const val MIN_RATING = 0.0
        const val MAX_RATING = 5.0
    }
}
