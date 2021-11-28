package it.polito.wa2.project.orderservice

import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
class OrderServiceApplication{

    @KafkaListener(topics = arrayOf("orderSagaRequest"), groupId = "group1")
    fun loadOrderSagaRequest( orderResponseDTO: OrderResponseDTO) {
        try{
            println("OrderRequest arrived on orderservice: $orderResponseDTO")
            println("TODO create order as requested, if UUID not present in DB") //TODO
        } catch (e: Exception){
            println("Exception on orderSagaRequest KafkaListener")
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
