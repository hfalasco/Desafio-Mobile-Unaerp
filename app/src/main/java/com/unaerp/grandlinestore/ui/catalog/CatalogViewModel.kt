package com.unaerp.grandlinestore.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.map
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.unaerp.grandlinestore.GrandLineStoreApp
import com.unaerp.grandlinestore.domain.model.Cart
import com.unaerp.grandlinestore.domain.model.ProductCategory
import com.unaerp.grandlinestore.domain.model.ProductFilter
import com.unaerp.grandlinestore.domain.model.filterBy
import com.unaerp.grandlinestore.domain.repository.ProductRepository
import com.unaerp.grandlinestore.ui.detail.AddToCartResult

/**
 * ViewModel compartilhado entre [MainActivity] (contador da sacola) e
 * [ProductListFragment] (lista e filtros), com escopo da Activity.
 */
class CatalogViewModel(
    private val repository: ProductRepository,
) : ViewModel() {

    private val _uiState = MutableLiveData(buildState(ProductFilter()))
    val uiState: LiveData<CatalogUiState> = _uiState

    private val _cart = MutableLiveData(Cart())
    val cart: LiveData<Cart> = _cart

    val cartQuantity: LiveData<Int> = _cart.map { it.totalQuantity }

    private val currentFilter: ProductFilter
        get() = _uiState.value?.filter ?: ProductFilter()

    fun onCategorySelected(category: ProductCategory?) {
        updateFilter(currentFilter.copy(category = category))
    }

    fun onOnlyInStockChanged(onlyInStock: Boolean) {
        updateFilter(currentFilter.copy(onlyInStock = onlyInStock))
    }

    /**
     * Adiciona à sacola o item retornado pela tela de detalhe.
     * @return `true` se a sacola mudou; `false` se o produto não existe ou o limite de estoque foi atingido.
     */
    fun addToCart(result: AddToCartResult): Boolean {
        val product = repository.getProductById(result.productId) ?: return false
        val currentCart = _cart.value ?: Cart()
        val updatedCart = currentCart.add(product, result.quantity)
        if (updatedCart == currentCart) return false
        _cart.value = updatedCart
        return true
    }

    private fun updateFilter(newFilter: ProductFilter) {
        if (newFilter == currentFilter) return
        _uiState.value = buildState(newFilter)
    }

    private fun buildState(filter: ProductFilter) = CatalogUiState(
        products = repository.getProducts().filterBy(filter),
        filter = filter,
    )

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as GrandLineStoreApp
                CatalogViewModel(app.container.productRepository)
            }
        }
    }
}
