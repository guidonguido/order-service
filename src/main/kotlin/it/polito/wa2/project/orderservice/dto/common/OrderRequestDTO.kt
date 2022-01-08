package it.polito.wa2.project.orderservice.dto.common

import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.domain.coreography.OrderRequest
import it.polito.wa2.project.orderservice.dto.OrderProductDTO
import it.polito.wa2.project.orderservice.dto.toOrderProductDTO

data class OrderRequestDTO(
    var uuid: String,
    var orderId: Long?,
    var buyerId: Long?,

    var deliveryName: String,
    var deliveryStreet: String,
    var deliveryZip: String,
    var deliveryCity: String,
    var deliveryNumber: String,

    var status: OrderStatus?,

    var orderProducts: Set<OrderProductDTO>,
    var totalPrice: Double?,

    var destinationWalletId: Long?,
    var sourceWalletId: Long,

    var transactionReason: String?
)



fun OrderRequest.toOrderRequestDTO() = OrderRequestDTO(
    uuid,
    orderId,
    buyerId,
    deliveryName,
    deliveryStreet,
    deliveryZip,
    deliveryCity,
    deliveryNumber,
    status,
    orderProducts.map{ it.toOrderProductDTO() }.toSet(),
    totalPrice,
    destinationWalletId,
    sourceWalletId,
    transactionReason
)
