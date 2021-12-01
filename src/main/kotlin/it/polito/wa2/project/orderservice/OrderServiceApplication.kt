package it.polito.wa2.project.orderservice

import it.polito.wa2.project.orderservice.dto.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
class OrderServiceApplication{

    // TODO change in arrayOf("orderWalletSagaRequest")
    @KafkaListener(topics = arrayOf("orderCatalogSagaRequest"), groupId = "group1")
    fun loadOrderSagaRequest( orderRequestDTO: OrderRequestDTO ) {
        try{
            println("OrderRequest arrived on orderservice: $orderRequestDTO")
            println("TODO create order as requested, if UUID not present in DB") //TODO
        } catch (e: Exception){
            println("Exception on orderSagaRequest KafkaListener")
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }

    /**
     *
    {
    "uuid": "hawjbq2",
    "buyerId": 11,
    "deliveryName": "Guido Ricioppo",
    "deliveryStreet": "via Saluzzo 13",
    "deliveryZip": "10111",
    "deliveryCity": "Torino",
    "deliveryNumber": "3433333333",
    "status": "ISSUED",
    "orderProducts": [
    {"purchasedProductId": 112,
    "amount": 2,
    "purchasedProductPrice": 12,
    "warehouseId": 3333
    }
    ],
    "totalPrice": 12,
    "destinationWalletId": 1,
    "sourceWalletId": 12,

    "transactionReason": "hell",
    "reasonDetail": 1
    }
     */
}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}
