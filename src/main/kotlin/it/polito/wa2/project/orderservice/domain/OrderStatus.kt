package it.polito.wa2.project.orderservice.domain

enum class OrderStatus {
    ISSUED,
    DELIVERING,
    DELIVERED,
    FAILED,
    CANCELED
}