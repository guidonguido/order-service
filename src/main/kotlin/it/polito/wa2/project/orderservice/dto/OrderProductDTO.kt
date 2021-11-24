package it.polito.wa2.project.orderservice.dto

import it.polito.wa2.project.orderservice.domain.OrderProduct
import java.math.BigInteger

data class OrderProductDTO(
    var purchasedProductId: Long,             // productId of purchased product
    var quantity: BigInteger,
    var purchasedProductPrice: Double,   // Product(productId)'s price
    var warehouseId: Long                    // warehouseId products are picked from
)

fun OrderProduct.toOrderProductDTO() = OrderProductDTO(
    purchasedProductId,
    quantity,
    purchasedProductPrice,
    warehouseId
)