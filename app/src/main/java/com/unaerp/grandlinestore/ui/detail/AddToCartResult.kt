package com.unaerp.grandlinestore.ui.detail

/** Dados devolvidos pela tela de detalhe quando o usuário adiciona um item à sacola. */
data class AddToCartResult(
    val productId: String,
    val productName: String,
    val quantity: Int,
)
