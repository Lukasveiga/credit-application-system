package br.com.diobootcamp.credit.application.system.exception

import br.com.diobootcamp.credit.application.system.services.exceptions.CreditNotFoundException
import br.com.diobootcamp.credit.application.system.services.exceptions.CustomerNotFoundException
import br.com.diobootcamp.credit.application.system.services.exceptions.InvalidDayFirstOfInstallmentException
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.stream().forEach {
            error: ObjectError ->
                val fieldName: String = (error as FieldError).field
                val messageError: String? = error.defaultMessage
                errors[fieldName] = messageError
        }
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request. Consult documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.simpleName.toString(),
                details = errors
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handlerDataAccessExceptions(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request. Consult documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.CONFLICT.value(),
                exception = ex.javaClass.simpleName.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message.toString())
            ), HttpStatus.CONFLICT
        )
    }

    @ExceptionHandler(InvalidDayFirstOfInstallmentException::class)
    fun handlerBusinessExceptions(ex: InvalidDayFirstOfInstallmentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity(
            ExceptionDetails(
                title = "Bad Request. Consult documentation",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                exception = ex.javaClass.simpleName.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message.toString())
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(CreditNotFoundException::class, CustomerNotFoundException::class)
    fun handlerNotFoundExceptions(ex: RuntimeException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity(
            ExceptionDetails(
                title = "Not found exception",
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                exception = ex.javaClass.simpleName.toString(),
                details = mutableMapOf(ex.cause.toString() to ex.message.toString())
            ), HttpStatus.NOT_FOUND
        )
    }

}