package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.CustomerDTO
import br.com.diobootcamp.credit.application.system.dto.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.dto.CustomerView
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
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

    @PostMapping
    fun saveCustomer(@RequestBody customerDto: CustomerDTO): ResponseEntity<String> {
        val savedCustomer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer ${savedCustomer.email} saved!")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable customerId: Long): ResponseEntity<CustomerView> {
        val customer = this.customerService.findById(customerId)
        return ResponseEntity.ok(CustomerView(customer))
    }

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") customerId: Long, @RequestBody customerUpdateDTO: CustomerUpdateDTO): ResponseEntity<CustomerView> {
        val updatedCustomer = this.customerService.update(customerId, customerUpdateDTO)
        return ResponseEntity.ok(CustomerView(updatedCustomer))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable customerId: Long): ResponseEntity.BodyBuilder {
        this.customerService.delete(customerId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
    }
}