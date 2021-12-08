package it.polito.wa2.project.orderservice.exceptions

class ExistingRequestException(override val message: String?): RuntimeException(message) {
}