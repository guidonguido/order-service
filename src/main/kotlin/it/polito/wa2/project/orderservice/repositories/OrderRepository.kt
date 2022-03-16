package it.polito.wa2.project.orderservice.repositories

import it.polito.wa2.project.orderservice.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository: JpaRepository<Order, Long> {

    fun findAllByBuyerId(buyerId: Long): Set<Order>

    fun findByIdAndBuyerId(id: Long, buyerId: Long): Optional<Order>
}