package com.unaerp.grandlinestore.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.unaerp.grandlinestore.GrandLineStoreApp
import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.repository.ProductRepository

/**
 * ViewModel da tela de detalhe.
 *
 * O id do produto chega pelos extras da Intent, que o Android repassa
 * automaticamente ao [SavedStateHandle]. A quantidade escolhida também fica
 * no [SavedStateHandle], sobrevivendo a rotação e à morte do processo.
 */
class ProductDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    repository: ProductRepository,
) : ViewModel() {

    /** `null` quando o id não foi enviado ou não corresponde a nenhum produto. */
    val product: Product? = savedStateHandle.get<String>(ProductDetailActivity.EXTRA_PRODUCT_ID)
        ?.let(repository::getProductById)

    val maxQuantity: Int = product?.stock?.coerceAtMost(MAX_QUANTITY_PER_ORDER) ?: 0

    val quantity: LiveData<Int> = savedStateHandle.getLiveData(KEY_QUANTITY, MIN_QUANTITY)

    val totalInCents: LiveData<Long> = quantity.map { selected ->
        (product?.finalPriceInCents ?: 0L) * selected
    }

    fun onQuantityChanged(newQuantity: Int) {
        val upperBound = maxOf(MIN_QUANTITY, maxQuantity)
        savedStateHandle[KEY_QUANTITY] = newQuantity.coerceIn(MIN_QUANTITY, upperBound)
    }

    /** Monta o resultado para a tela anterior; `null` se o produto não pode ser comprado. */
    fun buildAddToCartResult(): AddToCartResult? {
        val purchasable = product?.takeIf { it.isAvailable } ?: return null
        return AddToCartResult(
            productId = purchasable.id,
            productName = purchasable.name,
            quantity = quantity.value ?: MIN_QUANTITY,
        )
    }

    companion object {
        const val MIN_QUANTITY = 1
        const val MAX_QUANTITY_PER_ORDER = 10
        private const val KEY_QUANTITY = "quantity"

        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as GrandLineStoreApp
                ProductDetailViewModel(createSavedStateHandle(), app.container.productRepository)
            }
        }
    }
}
