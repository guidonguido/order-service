package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderProduct
import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.domain.coreography.OrderRequest
import it.polito.wa2.project.orderservice.dto.*
import it.polito.wa2.project.orderservice.dto.common.NotificationRequestDTO
import it.polito.wa2.project.orderservice.dto.common.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.common.toOrderRequestDTO
import it.polito.wa2.project.orderservice.exceptions.ExistingRequestException
import it.polito.wa2.project.orderservice.exceptions.InvalidOperationException
import it.polito.wa2.project.orderservice.repositories.OrderRepository
import it.polito.wa2.project.orderservice.exceptions.NotFoundException
import it.polito.wa2.project.orderservice.repositories.coreography.OrderRequestRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImpl( private val orderRepository: OrderRepository,
                        private val orderRequestRepository: OrderRequestRepository,
                        val kafkaTemplateError: KafkaTemplate<String, OrderResponseDTO>,
                        val kafkaTemplateRequest: KafkaTemplate<String, OrderRequestDTO>,
                        val kafkaTemplateNotification: KafkaTemplate<String, NotificationRequestDTO>
): OrderService {

    override fun getOrders(): Set<OrderDTO> =
        orderRepository.findAll().map { it.toOrderDTO() }.toSet()

    override fun getOrder(orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist")

        return order.get().toOrderDTO()
    }

    override fun getBuyerOrder(orderId: Long, buyerId: Long): OrderDTO {
        val order = orderRepository.findByIdAndBuyerId(orderId, buyerId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist for the user")

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

        val updatedOrder = order.get()
        updatedOrder.status = status

        val orderDTO: OrderDTO = orderRepository.save(updatedOrder).toOrderDTO()

        publishNotification(NotificationRequestDTO(
            messageObject = "Order Status Changed",
            message = "Dear customer,\nOrder with id ${updatedOrder.getId()} changet its status to ${status}\n\n" +
                    "Best Regards,\nAGTeam OrderService\n",
            userId = updatedOrder.buyerId
        ))

        return orderDTO;
    }

    override fun deleteOrder(orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")
        if (order.get().status != OrderStatus.ISSUED) throw InvalidOperationException("[OrderService Exception] Selected order is in an advanced state, no deletion is possible")
        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

        publishNotification(NotificationRequestDTO(
            messageObject = "Order CANCELED",
            message = "Dear customer,\nOrder with id ${order.get().getId()} has been deleted as requested\n\n" +
                    "Best Regards,\nAGTeam OrderService\n",
            userId = order.get().buyerId
        ))

        publishNotification(NotificationRequestDTO(
            messageObject = "Order CANCELED ADMIN INFO",
            message = "Dear ADMIN,\nOrder with id ${order.get().getId()} has been deleted as requested by customer ${order.get().buyerId}\n\n" +
                    "Best Regards,\nAGTeam OrderService\n",
            userId = null
        ))

        return deletedOrder.toOrderDTO()
    }

    override fun deleteOrderByRequest(orderId: Long, uuid: String) {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")
        if (order.get().status != OrderStatus.ISSUED) throw InvalidOperationException("[OrderService Exception] Selected order is in an advanced state, no deletion is possible")

        val orderRequest = orderRequestRepository.findByUuid(uuid)

        if(orderRequest.isNotEmpty()) orderRequest.first().status = OrderStatus.FAILED;
        else return

        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

    }

    override fun deleteBuyerOrder(orderId: Long, buyerId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")
        if (order.get().status != OrderStatus.ISSUED) throw InvalidOperationException("[OrderService Exception] Selected order is in an advanced state, no deletion is possible")
        if (order.get().buyerId != buyerId) throw NotFoundException("[OrderService Exception] Selected orderId does not belongs to selected buyer, no deletion is possible")

        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

        publishNotification(NotificationRequestDTO(
            messageObject = "Order CANCELED",
            message = "Dear customer,\nOrder with id ${order.get().getId()} has been deleted as requested\n\n" +
                    "Best Regards,\nAGTeam OrderService\n",
            userId = order.get().buyerId
        ))

        publishNotification(NotificationRequestDTO(
            messageObject = "Order CANCELED ADMIN INFO",
            message = "Dear ADMIN,\nOrder with id ${order.get().getId()} has been deleted as requested by customer ${order.get().buyerId}\n\n" +
                    "Best Regards,\nAGTeam OrderService\n",
            userId = null
        ))

        return deletedOrder.toOrderDTO()
    }


    override fun addOrderByRequest( orderRequestDTO: OrderRequestDTO ): OrderRequestDTO {

        val existingRequest = orderRequestRepository.findByUuid(orderRequestDTO.uuid)

        if( existingRequest.isNotEmpty() ) throw ExistingRequestException("OrderRequest[uuid:${orderRequestDTO.uuid}] has already been processed ")

        val newOrder =
            Order(orderRequestDTO.buyerId!!,
                orderRequestDTO.deliveryName,
                orderRequestDTO.deliveryStreet,
                orderRequestDTO.deliveryZip,
                orderRequestDTO.deliveryCity,
                orderRequestDTO.deliveryNumber,
                orderRequestDTO.status!!,
                )

        orderRequestDTO.orderProducts.forEach{
            newOrder.addOrderProduct( OrderProduct(null,
                it.purchasedProductId,
                it.quantity,
                it.purchasedProductPrice,
                it.warehouseId ))}

        val addedOrder: Order = orderRepository.save(newOrder)

        return orderRequestRepository.save(OrderRequest(
            orderRequestDTO.uuid,
            addedOrder.getId(),
            orderRequestDTO.buyerId,
            orderRequestDTO.deliveryName,
            orderRequestDTO.deliveryStreet,
            orderRequestDTO.deliveryZip,
            orderRequestDTO.deliveryCity,
            orderRequestDTO.deliveryNumber,
            OrderStatus.fromString(addedOrder.status.toString()),
            mutableSetOf(),
            orderRequestDTO.totalPrice,
            orderRequestDTO.destinationWalletId,
            orderRequestDTO.sourceWalletId,
            orderRequestDTO.transactionReason)).toOrderRequestDTO()
    }

    override fun publishOrderRequest( orderRequestDTO: OrderRequestDTO ){

        val future: ListenableFuture<SendResult<String, OrderRequestDTO>> = kafkaTemplateRequest.send("orderOrderSagaRequest", orderRequestDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderRequestDTO>> {
            override fun onSuccess(result: SendResult<String, OrderRequestDTO>?) {
                println("Sent message orderResponseDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message orderResponseDTO")
            }
        })
    }

    override fun publishOrderSagaError( orderResponseDTO: OrderResponseDTO ){

        val future: ListenableFuture<SendResult<String, OrderResponseDTO>> = kafkaTemplateError.send("orderOrderSagaResponse", orderResponseDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, OrderResponseDTO>> {
            override fun onSuccess(result: SendResult<String, OrderResponseDTO>?) {
                println("Sent message orderRequestDTO after Error")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message orderRequestDTO after Error")
            }
        })
    }

    override fun publishNotification( notificationRequestDTO: NotificationRequestDTO ){

        val future: ListenableFuture<SendResult<String, NotificationRequestDTO>> = kafkaTemplateNotification.send("emailRequest", notificationRequestDTO)
        future.addCallback(object: ListenableFutureCallback<SendResult<String, NotificationRequestDTO>> {
            override fun onSuccess(result: SendResult<String, NotificationRequestDTO>?) {
                println("Sent message notificationRequestDTO")
            }

            override fun onFailure(ex: Throwable) {
                println("Unable to send message notificationRequestDTO")
            }
        })
    }

}