package it.polito.wa2.project.orderservice.dto

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderProductDTO

data class PatchOrderDTO(
    var orderId: Long?, // Null if Domain Object used to add new Order

    var buyerId: Long?,

    var deliveryName: String?,
    var deliveryStreet: String?,
    var deliveryZip: String?,
    var deliveryCity: String?,
    var deliveryNumber: String?,

    var status: OrderStatus,

    var orderProducts: Set<OrderProductDTO>?
)


// TODO is setter orderProducts LIST required?



