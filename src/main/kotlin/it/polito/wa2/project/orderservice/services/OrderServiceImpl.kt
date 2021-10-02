package it.polito.wa2.project.orderservice.services

import it.polito.wa2.project.orderservice.repositories.OrderRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class OrderServiceImpl( private val orderRepository: OrderRepository): OrderService{
}