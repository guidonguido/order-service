package it.polito.wa2.project.orderservice.dto

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderStatus

data class OrderDTO(
    var buyerId: Long,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus,

    var orderProducts: Set<OrderProductDTO>? = null
)

fun Order.toOrderDTO() = OrderDTO(
    buyerId,
    deliveryName,
    deliveryStreet,
    deliveryZip,
    deliveryCity,
    deliveryNumber,
    status
)

// TODO is setter orderProducts LIST required?

