package it.polito.wa2.project.orderservice.controllers

import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.exceptions.NotFoundException
import it.polito.wa2.project.orderservice.services.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/orders")
@Validated
class OrderController( val orderService: OrderService ){

    @GetMapping()
    fun getOrders(): ResponseEntity<Set<OrderDTO>> {
        val orderDTOs = orderService.getOrders()

        return ResponseEntity(orderDTOs, HttpStatus.OK)
    }

    @GetMapping("/{orderId}")
    fun getOrder(
        @PathVariable
        orderId:Long
    ): ResponseEntity<OrderDTO>{
        val orderDTO = orderService.getOrder(orderId)

        return ResponseEntity(orderDTO, HttpStatus.OK)
    }

    @GetMapping("/buyer")
    fun getBuyerOrders(
        @RequestParam
        @NotNull(message = "'from' timestamp is required")
        @Positive(message = "Insert a valid 'from' timestamp")
        id: Long? = null,
    ): ResponseEntity<Set<OrderDTO>>{
        val orderDTOs = orderService.getBuyerOrders(id!!)

        return ResponseEntity(orderDTOs, HttpStatus.OK)
    }

    @PostMapping()
    fun addOrder(
        @RequestBody
        orderDTO: OrderDTO
    ): ResponseEntity<OrderDTO> {
        val newOrderDTO = orderService.addOrder(orderDTO)

        /** TODO
         * Use NotificationService to send an email to ADMINs and USERs
         * about the order status
         */

        return ResponseEntity(newOrderDTO, HttpStatus.OK)
    }

    /**
     * Test with
    {
    "buyerId": 11,
    "deliveryName": "Guido Ricioppo",
    "deliveryStreet": "via Saluzzo 13",
    "deliveryZip": "10111",
    "deliveryCity": "Torino",
    "deliveryNumber": "3433333333",
    "status": "ISSUED",
    "orderProducts": [
    {"purchasedProductId": 1212,             // productId of purchased product
    "amount": 2,
    "purchasedProductPrice": 12,   // Product(productId)'s price
    "warehouseId": 3333
    }
    ]
    }
     */

    /**
     *  NOTICE! Only status property of an existing Order can be updated
     *  In order to modify the order, it must be deleted if possible, than re-created
     */
    @PatchMapping("/{orderId}", consumes = ["application/json"])
    fun updateOrder(
        @PathVariable
        orderId: Long,
        @RequestBody
        status: String
    ): ResponseEntity<OrderDTO>{
        val updatedOrderDTO = orderService.updateOrder(orderId, OrderStatus.fromString(status)
            ?: throw NotFoundException("[OrderService Exception] Inserted value ${status} is not a valid OrderStatus"))

        /* TODO
         * Use NotificationService to send an email to ADMINs and USERs
         * about the updated order status
         */

        return ResponseEntity(updatedOrderDTO, HttpStatus.OK)
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(
        @PathVariable
        orderId: Long
    ): ResponseEntity<OrderDTO> {
        val deletedOrderDTO = orderService.deleteOrder(orderId)

        /* TODO
         * Use NotificationService to send an email to ADMINs and USERs
         * about the updated order status
         */

        return ResponseEntity(deletedOrderDTO, HttpStatus.OK)
    }


}