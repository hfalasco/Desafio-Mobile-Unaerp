package com.unaerp.grandlinestore.domain.model

/** Situação do estoque de um produto, derivada da quantidade disponível. */
enum class StockStatus {
    AVAILABLE,
    LOW,
    SOLD_OUT,
}
