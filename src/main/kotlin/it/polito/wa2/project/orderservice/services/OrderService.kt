package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.dto.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO

interface OrderService {
    fun getOrders(): Set<OrderDTO>

    fun getOrder(orderId: Long): OrderDTO

    fun getBuyerOrders(buyerId: Long): Set<OrderDTO>

    fun addOrder(order: OrderDTO): OrderDTO

    fun updateOrder(orderId: Long, status: OrderStatus): OrderDTO

    fun deleteOrder(orderId: Long): OrderDTO

    fun deleteBuyerOrder(orderId: Long, buyerId: Long): OrderDTO

    fun addOrderByRequest( orderRequestDTO: OrderRequestDTO): OrderDTO

    fun publishOrderSagaError( orderResponseDTO: OrderResponseDTO )

    fun publishOrderResponse( orderResponseDTO: OrderResponseDTO)

}