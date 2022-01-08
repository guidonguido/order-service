package it.polito.wa2.project.orderservice.dto.common

data class NotificationRequestDTO (
    val messageObject: String,
    val message: String,
    val userId: Long?,     // if null send to all service Admins
)