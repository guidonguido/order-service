package it.polito.wa2.project.orderservice

import it.polito.wa2.project.orderservice.dto.OrderRequestDTO
import it.polito.wa2.project.orderservice.dto.OrderResponseDTO
import it.polito.wa2.project.orderservice.exceptions.ExistingRequestException
import it.polito.wa2.project.orderservice.services.OrderService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.kafka.annotation.KafkaListener

@SpringBootApplication
@EnableEurekaClient
class OrderServiceApplication( val orderService: OrderService ){

    @KafkaListener(topics = arrayOf("orderWalletSagaRequest"), groupId = "group1")
    fun loadOrderSagaRequest( orderRequestDTO: OrderRequestDTO ) {
        try{
            println("OrderRequest arrived on orderservice: $orderRequestDTO")
            orderService.addOrderByRequest( orderRequestDTO );
        }catch (e: ExistingRequestException){
            println("ExistingRequestException: ${e.message}");
            val orderResponse = OrderResponseDTO(null, orderRequestDTO.uuid, -2);
            orderService.publishOrderSagaError(orderResponse);
        } catch (e: Exception){
            println("loadOrderSagaRequest generic Error");
            val orderResponse = OrderResponseDTO(null, orderRequestDTO.uuid, -1);
            orderService.publishOrderSagaError(orderResponse);
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
