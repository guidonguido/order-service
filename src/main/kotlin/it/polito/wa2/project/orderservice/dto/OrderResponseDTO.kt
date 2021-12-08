package it.polito.wa2.project.orderservice.dto

data class OrderResponseDTO(
    val orderId: Long?,
    val uuid: String,
    val exitStatus: Long
)