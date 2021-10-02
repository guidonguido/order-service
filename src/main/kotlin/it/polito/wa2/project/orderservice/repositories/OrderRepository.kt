package it.polito.wa2.project.orderservice.repositories

import it.polito.wa2.project.orderservice.domain.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CrudRepository<Order, Long> {
}