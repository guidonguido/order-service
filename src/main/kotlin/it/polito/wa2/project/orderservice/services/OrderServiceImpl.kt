package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderProduct
import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.dto.toOrderDTO
import it.polito.wa2.project.orderservice.repositories.OrderRepository
import it.polito.wa2.project.orderservice.exceptions.NotFoundException
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import it.polito.wa2project.wa2projectcatalogservice.dto.OrderResponseDTO
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImpl( private val orderRepository: OrderRepository,
                        val kafkaTemplateOrderResponse: KafkaTemplate<String, OrderResponseDTO>,
                        val kafkaTemplateOrderSagaError: KafkaTemplate<String, OrderRequestDTO>
): OrderService {

    override fun getOrders(): Set<OrderDTO> =
        orderRepository.findAll().map { it.toOrderDTO() }.toSet()

    override fun getOrder(orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist")

        return order.get().toOrderDTO()
    }

    override fun getBuyerOrders(buyerId: Long): Set<OrderDTO> =
        orderRepository.findAllByBuyerId(buyerId).map{ it.toOrderDTO() }.toSet()

    override fun addOrder(order: OrderDTO): OrderDTO {

        val newOrder =
            Order(order.buyerId,
                order.deliveryName,
                order.deliveryStreet,
                order.deliveryZip,
                order.deliveryCity,
                order.deliveryNumber,
                order.status)

        order.orderProducts.forEach{
            newOrder.addOrderProduct( OrderProduct(null,
                it.purchasedProductId,
                it.quantity,
                it.purchasedProductPrice,
                it.warehouseId ))}

        return orderRepository.save(newOrder).toOrderDTO()
    }

    override fun updateOrder(orderId: Long, status: OrderStatus): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist")

        var updatedOrder = order.get()
        updatedOrder.status = status

        return  orderRepository.save(updatedOrder).toOrderDTO()
    }

    override fun deleteOrder(orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")

        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

        return deletedOrder.toOrderDTO()
    }

    override fun deteteBuyerOrder(buyerId: Long, orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")
        if (order.get().buyerId != buyerId) throw NotFoundException("[OrderService Exception] Selected orderId does not belongs to selected buyer, no deletion is possible")

        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

        return deletedOrder.toOrderDTO()
    }

    // Send final Response to CatalogService
    fun sendKafkaOrderResponse(orderResponseDTO: OrderResponseDTO) {

        val future: ListenableFuture<SendResult<String, OrderResponseDTO>> = kafkaTemplateOrderResponse.send("orderSagaResponse", orderResponseDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderResponseDTO>> {
            override fun onSuccess(result: SendResult<String, OrderResponseDTO>?) {
                println("Sent message OrderResponseDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message OrderResponseDTO")
            }
        })
    }

    // Send error notification to WarehouseService
    fun sendKafkaOrderSagaError(orderRequestDTO: OrderRequestDTO) {

        val future: ListenableFuture<SendResult<String, OrderRequestDTO>> = kafkaTemplateOrderSagaError.send("orderSagaResponse", orderRequestDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderRequestDTO>> {
            override fun onSuccess(result: SendResult<String, OrderRequestDTO>?) {
                println("Sent message orderRequestDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message orderRequestDTO")
            }
        })
    }

}