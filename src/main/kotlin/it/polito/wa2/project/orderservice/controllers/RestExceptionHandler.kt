package it.polito.wa2.project.orderservice.controllers

import it.polito.wa2.project.orderservice.exceptions.InvalidOperationException
import it.polito.wa2.project.orderservice.exceptions.NotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler(){

    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(InvalidOperationException::class)
    protected fun handleInvalidOperationException(e: InvalidOperationException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)

}// RestExceptionHandler