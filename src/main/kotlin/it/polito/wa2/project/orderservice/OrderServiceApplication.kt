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
class OrderServiceApplication( val orderService: OrderService ) {

    @KafkaListener(topics = arrayOf("orderWarehouseSagaRequest"), groupId = "group1", containerFactory = "kafkaListenerContainerFactoryRequest")
    fun loadOrderSagaRequest(orderRequestDTO: OrderRequestDTO) {
        try {
            println("OrderRequest arrived on orderservice: $orderRequestDTO")
            orderService.addOrderByRequest(orderRequestDTO);
        } catch (e: ExistingRequestException) {
            println("ExistingRequestException: ${e.message}");
            val orderResponse = OrderResponseDTO(null, orderRequestDTO.uuid, -2);
            orderService.publishOrderSagaError(orderResponse);
        } catch (e: Exception) {
            println("loadOrderSagaRequest generic Error");
            val orderResponse = OrderResponseDTO(null, orderRequestDTO.uuid, -1);
            orderService.publishOrderSagaError(orderResponse);
        }
        // TODO Send email to buyerId with OrderId, OrderStatus
    }

    @KafkaListener(topics = arrayOf("orderWalletSagaRequest"), groupId = "group1", containerFactory = "kafkaListenerContainerFactoryResponse")
    fun loadOrderSagaRequest(orderResponseDTO: OrderResponseDTO) {
        if (orderResponseDTO.exitStatus == -1L) {
            println("OrderResonse [Saga Error] arrived on orderservice: $orderResponseDTO")
            orderService.deleteOrder(orderResponseDTO.orderId!!);
            orderService.publishOrderSagaError(orderResponseDTO);
        }
        // TODO Send email to buyerId with OrderId, OrderStatus

        /**
         *
        {
        "buyerId": 1,
        "deliveryName": "Guido Ricioppo",
        "deliveryStreet": "via Saluzzo 13",
        "deliveryZip": "10111",
        "deliveryCity": "Torino",
        "deliveryNumber": "3433333333",
        "status": "ISSUED",
        "orderProducts": [
        {"purchasedProductId": 1,
        "quantity": 2
        }
        ],
        "destinationWalletId": 2,
        "sourceWalletId": 1,

        "transactionReason": "hell"
        }
         */
    }

}

fun main(args: Array<String>) {
    runApplication<OrderServiceApplication>(*args)
}