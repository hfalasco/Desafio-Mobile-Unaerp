package com.unaerp.grandlinestore.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.databinding.FragmentProductListBinding
import com.unaerp.grandlinestore.databinding.ViewCategoryChipBinding
import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.model.ProductCategory
import com.unaerp.grandlinestore.ui.common.applySystemBarsPadding
import com.unaerp.grandlinestore.ui.common.labelRes
import com.unaerp.grandlinestore.ui.detail.AddToCartResult
import com.unaerp.grandlinestore.ui.detail.OpenProductDetailContract

/**
 * Lista de produtos com filtros por categoria e estoque.
 *
 * Segue o ciclo de vida de Fragment com ViewBinding: o binding é criado em
 * [onCreateView] e liberado em [onDestroyView], pois a View do Fragment pode
 * ser destruída enquanto a instância do Fragment continua viva.
 */
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding
        get() = checkNotNull(_binding) { "Binding acessado fora do ciclo de vida da View." }

    private val viewModel: CatalogViewModel by activityViewModels { CatalogViewModel.Factory }

    private val productAdapter = ProductAdapter(onProductClick = ::openProductDetail)

    // Intent explícita para a tela de detalhe, com retorno do item adicionado à sacola.
    private val productDetailLauncher = registerForActivityResult(OpenProductDetailContract()) { result ->
        result?.let(::onProductAddedToCart)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val initialFilter = viewModel.uiState.value?.filter

        setupRecyclerView()
        setupCategoryChips(selectedCategory = initialFilter?.category)
        setupStockSwitch(onlyInStock = initialFilter?.onlyInStock ?: false)

        viewModel.uiState.observe(viewLifecycleOwner, ::render)
    }

    override fun onDestroyView() {
        // Desconecta o adapter para que ele não mantenha referência à RecyclerView destruída.
        binding.rvProducts.adapter = null
        _binding = null
        super.onDestroyView()
    }

    private fun setupRecyclerView() {
        binding.rvProducts.adapter = productAdapter
        binding.rvProducts.applySystemBarsPadding(applyBottom = true)
    }

    /** Infla dinamicamente um chip reutilizável (`view_category_chip.xml`) por categoria. */
    private fun setupCategoryChips(selectedCategory: ProductCategory?) {
        val chipGroup = binding.chipGroupCategories
        val options: List<ProductCategory?> = listOf(null) + ProductCategory.entries

        options.forEach { category ->
            val chip = ViewCategoryChipBinding.inflate(layoutInflater, chipGroup, false).root.apply {
                id = View.generateViewId()
                tag = category
                text = category?.let { getString(it.labelRes) } ?: getString(R.string.category_all)
                isChecked = category == selectedCategory
            }
            chipGroup.addView(chip)
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedChip = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            viewModel.onCategorySelected(checkedChip?.tag as ProductCategory?)
        }
    }

    private fun setupStockSwitch(onlyInStock: Boolean) {
        binding.switchOnlyInStock.isChecked = onlyInStock
        binding.switchOnlyInStock.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onOnlyInStockChanged(isChecked)
        }
    }

    private fun render(state: CatalogUiState) {
        productAdapter.submitList(state.products)
        binding.tvResultsCount.text = resources.getQuantityString(
            R.plurals.catalog_results_count,
            state.products.size,
            state.products.size,
        )
        binding.rvProducts.isVisible = !state.isEmpty
        binding.layoutEmptyState.isVisible = state.isEmpty
    }

    private fun openProductDetail(product: Product) {
        productDetailLauncher.launch(product.id)
    }

    private fun onProductAddedToCart(result: AddToCartResult) {
        val added = viewModel.addToCart(result)
        val message = if (added) {
            getString(R.string.cart_added_message, result.quantity, result.productName)
        } else {
            getString(R.string.cart_stock_limit_message, result.productName)
        }
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
