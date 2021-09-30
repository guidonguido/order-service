package it.polito.wa2.project.orderservice.dto

import it.polito.wa2.project.orderservice.domain.OrderProduct
import java.math.BigDecimal

data class OrderProductDTO(
    var purchasedProductId: Long,             // productId of purchased product
    var amount: Long,
    var purchasedProductPrice: BigDecimal,   // Product(productId)'s price
    var warehouseId: Long                    // warehouseId products are picked from
)

fun OrderProduct.toOrderProductDTO() = OrderProductDTO(
    purchasedProductId,
    amount,
    purchasedProductPrice,
    warehouseId
)