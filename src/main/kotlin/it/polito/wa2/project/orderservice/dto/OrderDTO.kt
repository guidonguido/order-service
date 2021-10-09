package it.polito.wa2.project.orderservice.dto

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderStatus

data class OrderDTO(
    var orderId: Long?, // Null if Domain Object used to add new Order

    var buyerId: Long,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus,

    var orderProducts: Set<OrderProductDTO>
)

fun Order.toOrderDTO() = OrderDTO(
    getId(),
    buyerId,
    deliveryName,
    deliveryStreet,
    deliveryZip,
    deliveryCity,
    deliveryNumber,
    status,
    orderProducts.map{ it.toOrderProductDTO() }.toSet()
)

// TODO is setter orderProducts LIST required?



