package com.unaerp.grandlinestore.data.mock

import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.model.ProductCategory

/**
 * Dados simulados (mocks) do catálogo. Os produtos, preços e estoques são fictícios.
 *
 * Alguns produtos omitem campos opcionais de propósito (descrição, personagem,
 * avaliação, promoção) para exercitar o tratamento de valores nulos na UI.
 */
object MockProductDataSource {

    val products: List<Product> = listOf(
        Product(
            id = "fig-luffy-gear5",
            name = "Figure Monkey D. Luffy – Gear 5",
            category = ProductCategory.FIGURE,
            priceInCents = 54_990,
            promotionalPriceInCents = 47_990,
            stock = 5,
            description = "Estátua em PVC de 28 cm com o capitão dos Chapéus de Palha " +
                "no auge do despertar. Acompanha base temática com nuvens.",
            character = "Monkey D. Luffy",
            arc = "Wano",
            rating = 4.9,
        ),
        Product(
            id = "fig-zoro-santoryu",
            name = "Figure Roronoa Zoro – Santoryu",
            category = ProductCategory.FIGURE,
            priceInCents = 42_990,
            stock = 2,
            description = "O espadachim em pose de ataque com as três espadas. " +
                "Pintura detalhada e efeito de corte em acrílico.",
            character = "Roronoa Zoro",
            arc = "Wano",
            rating = 4.8,
        ),
        Product(
            id = "fig-law-room",
            name = "Figure Trafalgar D. Water Law",
            category = ProductCategory.FIGURE,
            priceInCents = 38_990,
            stock = 0,
            character = "Trafalgar D. Water Law",
            arc = "Punk Hazard",
        ),
        Product(
            id = "manga-vol-1",
            name = "Mangá One Piece – Volume 1",
            category = ProductCategory.MANGA,
            priceInCents = 3_490,
            stock = 30,
            description = "Romance Dawn: o início da jornada de Luffy rumo à Grand Line " +
                "em busca do One Piece.",
            character = "Monkey D. Luffy",
            arc = "Romance Dawn",
            rating = 5.0,
        ),
        Product(
            id = "manga-box-east-blue",
            name = "Box Saga East Blue (Vol. 1 a 12)",
            category = ProductCategory.MANGA,
            priceInCents = 42_990,
            promotionalPriceInCents = 36_990,
            stock = 4,
            description = "Box colecionável com os 12 primeiros volumes e pôster exclusivo " +
                "do Going Merry.",
            arc = "East Blue",
            rating = 4.9,
        ),
        Product(
            id = "manga-vol-100",
            name = "Mangá One Piece – Volume 100",
            category = ProductCategory.MANGA,
            priceInCents = 3_990,
            stock = 12,
            arc = "Wano",
            rating = 4.7,
        ),
        Product(
            id = "card-booster-op01",
            name = "Booster One Piece Card Game – Romance Dawn",
            category = ProductCategory.CARD_GAME,
            priceInCents = 2_990,
            stock = 50,
            description = "Booster com 12 cartas aleatórias da primeira coleção do card game.",
            rating = 4.6,
        ),
        Product(
            id = "card-starter-straw-hat",
            name = "Starter Deck Bando do Chapéu de Palha",
            category = ProductCategory.CARD_GAME,
            priceInCents = 8_990,
            promotionalPriceInCents = 7_990,
            stock = 8,
            description = "Deck pronto para jogar com Luffy como líder e toda a tripulação.",
            character = "Monkey D. Luffy",
            rating = 4.5,
        ),
        Product(
            id = "acc-straw-hat",
            name = "Réplica do Chapéu de Palha",
            category = ProductCategory.ACCESSORY,
            priceInCents = 12_990,
            stock = 3,
            description = "Réplica em palha natural com fita vermelha costurada. " +
                "Tamanho adulto ajustável.",
            character = "Monkey D. Luffy",
            rating = 4.4,
        ),
        Product(
            id = "acc-jolly-roger-flag",
            name = "Bandeira Jolly Roger dos Chapéus de Palha",
            category = ProductCategory.ACCESSORY,
            priceInCents = 6_990,
            promotionalPriceInCents = 5_490,
            stock = 0,
            description = "Bandeira de 90 × 150 cm para hastear no seu navio (ou no quarto).",
        ),
        Product(
            id = "acc-den-den-mushi",
            name = "Luminária Den Den Mushi",
            category = ProductCategory.ACCESSORY,
            priceInCents = 15_990,
            stock = 6,
            rating = 4.2,
        ),
    )
}
