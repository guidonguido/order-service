package it.polito.wa2.project.orderservice.controllers

import it.polito.wa2.project.orderservice.domain.OrderStatus
import it.polito.wa2.project.orderservice.dto.OrderDTO
import it.polito.wa2.project.orderservice.services.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/{buyerId}")
    fun getBuyerOrders(
        @PathVariable
        buyerId:Long
    ): ResponseEntity<Set<OrderDTO>>{
        val orderDTOs = orderService.getBuyerOrders(buyerId)

        return ResponseEntity(orderDTOs, HttpStatus.OK)
    }

    @PostMapping()
    fun addOrder(
        @RequestBody
        orderDTO: OrderDTO
    ): ResponseEntity<OrderDTO> {
        val newOrderDTO = orderService.addOrder(orderDTO)

        /* TODO
         * Use NotificationService to send an email to ADMINs and USERs
         * about the order status
         */

        return ResponseEntity(orderDTO, HttpStatus.OK)
    }

    /**
     *  NOTICE! Only status property of an existing Order can be updated
     *  In order to modify the order, it must be deleted if possible, than re-created
     */
    @PatchMapping( "/{orderId}")
    fun updateOrder(
        @PathVariable
        orderId: Long,
        @RequestBody
        status: OrderStatus
    ): ResponseEntity<OrderDTO>{
        val updatedOrderDTO = orderService.updateOrder(orderId, status)

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