package com.unaerp.grandlinestore.ui.common

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unaerp.grandlinestore.R
import com.unaerp.grandlinestore.domain.model.Product
import com.unaerp.grandlinestore.domain.model.ProductCategory
import com.unaerp.grandlinestore.domain.model.StockStatus

/*
 * Mapeamentos de modelos de domínio para recursos Android.
 * Mantê-los aqui deixa o domínio livre de dependências do framework.
 */

@get:StringRes
val ProductCategory.labelRes: Int
    get() = when (this) {
        ProductCategory.FIGURE -> R.string.category_figure
        ProductCategory.MANGA -> R.string.category_manga
        ProductCategory.CARD_GAME -> R.string.category_card_game
        ProductCategory.ACCESSORY -> R.string.category_accessory
    }

@get:DrawableRes
val ProductCategory.iconRes: Int
    get() = when (this) {
        ProductCategory.FIGURE -> R.drawable.ic_figure
        ProductCategory.MANGA -> R.drawable.ic_manga
        ProductCategory.CARD_GAME -> R.drawable.ic_card_game
        ProductCategory.ACCESSORY -> R.drawable.ic_straw_hat
    }

/**
 * Foto do produto; `null` quando ainda não há imagem (a UI usa então o ícone da categoria).
 *
 * As capas de mangá são as do acervo do projeto: a de `manga-vol-1` é do volume 9 e a de
 * `manga-vol-100` é do volume 103.
 */
@get:DrawableRes
val Product.imageRes: Int?
    get() = when (id) {
        "fig-luffy-gear5" -> R.drawable.img_fig_luffy_gear5
        "fig-zoro-santoryu" -> R.drawable.img_fig_zoro_santoryu
        "fig-law-room" -> R.drawable.img_fig_law_room
        "manga-vol-1" -> R.drawable.img_manga_vol_1
        "manga-box-east-blue" -> R.drawable.img_manga_box_east_blue
        "manga-vol-100" -> R.drawable.img_manga_vol_100
        "card-booster-op01" -> R.drawable.img_card_booster_op01
        "card-starter-shanks" -> R.drawable.img_card_starter_shanks
        "acc-straw-hat" -> R.drawable.img_acc_straw_hat
        "acc-jolly-roger-flag" -> R.drawable.img_acc_jolly_roger_flag
        "acc-den-den-mushi" -> R.drawable.img_acc_den_den_mushi
        else -> null
    }

@get:ColorRes
val StockStatus.colorRes: Int
    get() = when (this) {
        StockStatus.AVAILABLE -> R.color.stock_available
        StockStatus.LOW -> R.color.stock_low
        StockStatus.SOLD_OUT -> R.color.stock_sold_out
    }

fun Product.stockLabel(resources: Resources): String = when (stockStatus) {
    StockStatus.AVAILABLE -> resources.getString(R.string.stock_available)
    StockStatus.LOW -> resources.getQuantityString(R.plurals.stock_low, stock, stock)
    StockStatus.SOLD_OUT -> resources.getString(R.string.stock_sold_out)
}

fun Product.discountLabel(resources: Resources): String? =
    discountPercentage?.let { resources.getString(R.string.product_discount_badge, it) }
