package it.polito.wa2.project.orderservice

import it.polito.wa2project.wa2projectcatalogservice.dto.OrderRequestDTO
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
class OrderServiceApplication

fun main(args: Array<String>) {

    @KafkaListener(topics = arrayOf("orderSagaRequest"), groupId = "group1")
    fun loadOrderSagaRequest( orderRequestDTO: OrderRequestDTO) {
        try{
            println("" + orderRequestDTO)
        } catch (e: Exception){
            println("Exception on KafkaListener")
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }


    runApplication<OrderServiceApplication>(*args)
}
