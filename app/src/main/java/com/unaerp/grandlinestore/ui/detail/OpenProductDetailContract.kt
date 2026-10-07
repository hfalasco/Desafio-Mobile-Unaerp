package com.unaerp.grandlinestore.ui.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/**
 * Contrato da navegação Catálogo -> Detalhe.
 *
 * - Ida: Intent **explícita** para [ProductDetailActivity] levando o id do produto.
 * - Volta: [AddToCartResult] quando o usuário adiciona o item, ou `null` se voltou sem adicionar.
 *
 * Centralizar as chaves dos extras aqui evita que as telas dependam de strings soltas.
 */
class OpenProductDetailContract : ActivityResultContract<String, AddToCartResult?>() {

    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, ProductDetailActivity::class.java)
            .putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, input)

    override fun parseResult(resultCode: Int, intent: Intent?): AddToCartResult? {
        if (resultCode != Activity.RESULT_OK || intent == null) return null

        val productId = intent.getStringExtra(RESULT_PRODUCT_ID) ?: return null
        val productName = intent.getStringExtra(RESULT_PRODUCT_NAME) ?: return null
        val quantity = intent.getIntExtra(RESULT_QUANTITY, 0).takeIf { it > 0 } ?: return null

        return AddToCartResult(productId, productName, quantity)
    }

    companion object {
        private const val RESULT_PRODUCT_ID = "com.unaerp.grandlinestore.result.PRODUCT_ID"
        private const val RESULT_PRODUCT_NAME = "com.unaerp.grandlinestore.result.PRODUCT_NAME"
        private const val RESULT_QUANTITY = "com.unaerp.grandlinestore.result.QUANTITY"

        fun createResultIntent(result: AddToCartResult): Intent = Intent()
            .putExtra(RESULT_PRODUCT_ID, result.productId)
            .putExtra(RESULT_PRODUCT_NAME, result.productName)
            .putExtra(RESULT_QUANTITY, result.quantity)
    }
}
