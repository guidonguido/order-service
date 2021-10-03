package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.domain.Order
import it.polito.wa2.project.orderservice.domain.OrderProduct
import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.dto.toOrderDTO
import it.polito.wa2.project.orderservice.repositories.OrderRepository
import javassist.NotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImpl( private val orderRepository: OrderRepository): OrderService {

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

        val newOrderProducts = order.orderProducts.map{ OrderProduct(newOrder,
                                                                     it.purchasedProductId,
                                                                     it.amount,
                                                                     it.purchasedProductPrice,
                                                                     it.warehouseId )}.toMutableSet()

        newOrder.orderProducts = newOrderProducts

        return orderRepository.saveAndFlush(newOrder).toOrderDTO()
    }

    override fun updateOrder(orderId: Long, status: OrderStatus): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no update is possible")

        var updatedOrder = order.get()
        updatedOrder.status = status

        return orderRepository.save(updatedOrder).toOrderDTO()

    }

    override fun deleteOrder(orderId: Long): OrderDTO {
        val order = orderRepository.findById(orderId)

        if (order.isEmpty) throw NotFoundException("[OrderService Exception] Selected orderId does not exist, no deletion is possible")

        val deletedOrder = order.get()
        orderRepository.delete(deletedOrder)

        return deletedOrder.toOrderDTO()
    }

}