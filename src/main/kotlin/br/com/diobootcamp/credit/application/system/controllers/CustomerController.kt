package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.credit.CreditView
import br.com.diobootcamp.credit.application.system.dto.customer.CustomerDTO
import br.com.diobootcamp.credit.application.system.dto.customer.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.dto.customer.CustomerView
import br.com.diobootcamp.credit.application.system.exception.ExceptionDetails
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/customers")
class CustomerController(private val customerService: CustomerService) {

    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Customer Created",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = CustomerView::class)))),
        ApiResponse(responseCode = "400", description = "Bad Request",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        ),
        ApiResponse(responseCode = "409", description = "Conflict check the documentation",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDTO): ResponseEntity<CustomerView> {
        val savedCustomer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(savedCustomer))
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer was found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = CustomerView::class)))),
        ApiResponse(responseCode = "404", description = "Customer not found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @GetMapping("/{customerId}")
    fun findById(@PathVariable customerId: Long): ResponseEntity<CustomerView> {
        val customer = this.customerService.findById(customerId)
        return ResponseEntity.ok(CustomerView(customer))
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer Updated",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = CustomerView::class)))),
        ApiResponse(responseCode = "400", description = "Bad Request",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        ),
        ApiResponse(responseCode = "404", description = "Customer not found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        ),
        ApiResponse(responseCode = "409", description = "Conflict check the documentation",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") customerId: Long,
                       @RequestBody @Valid customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerView> {
        val updatedCustomer = this.customerService.update(customerId, customerUpdateDTO)
        return ResponseEntity.ok(CustomerView(updatedCustomer))
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer Deleted"),
        ApiResponse(responseCode = "404", description = "Customer not found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @DeleteMapping("/{customerId}")
    fun deleteById(@PathVariable customerId: Long): ResponseEntity<Unit> {
        this.customerService.delete(customerId)
        return ResponseEntity.noContent().build()
    }
}