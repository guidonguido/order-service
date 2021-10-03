package it.polito.wa2.project.orderservice.exceptions

class NotFoundException(override val message: String?): RuntimeException(message)