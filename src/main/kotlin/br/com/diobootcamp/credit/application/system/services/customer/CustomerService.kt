package br.com.diobootcamp.credit.application.system.services.customer

import br.com.diobootcamp.credit.application.system.dto.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.entities.Customer

interface CustomerService {
    fun save(customer: Customer): Customer
    fun findById(customerId: Long): Customer

    fun update(customerId: Long, customerUpdateDTO: CustomerUpdateDTO): Customer
    fun delete(customerId: Long): Unit
}