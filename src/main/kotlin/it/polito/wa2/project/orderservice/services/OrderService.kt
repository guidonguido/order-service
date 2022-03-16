package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.dto.common.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import it.polito.wa2.project.orderservice.dto.common.NotificationRequestDTO

interface OrderService {
    fun getOrders(): Set<OrderDTO>

    fun getOrder(orderId: Long): OrderDTO

    fun getBuyerOrder(orderId: Long, buyerId: Long): OrderDTO

    fun getBuyerOrders(buyerId: Long): Set<OrderDTO>

    fun addOrder(order: OrderDTO): OrderDTO

    fun updateOrder(orderId: Long, status: OrderStatus): OrderDTO

    fun deleteOrder(orderId: Long): OrderDTO

    fun deleteOrderByRequest(orderId: Long, uuid: String)

    fun deleteBuyerOrder(orderId: Long, buyerId: Long): OrderDTO

    fun addOrderByRequest( orderRequestDTO: OrderRequestDTO): OrderRequestDTO

    fun publishOrderSagaError( orderResponseDTO: OrderResponseDTO)

    fun publishOrderRequest( orderRequestDTO: OrderRequestDTO)

    fun publishNotification( notificationRequestDTO: NotificationRequestDTO)

}