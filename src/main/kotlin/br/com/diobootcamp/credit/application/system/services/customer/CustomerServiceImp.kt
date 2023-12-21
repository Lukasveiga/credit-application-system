package br.com.diobootcamp.credit.application.system.services.customer

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import br.com.diobootcamp.credit.application.system.services.exceptions.BusinessExcetion
import org.springframework.stereotype.Service

@Service
class CustomerServiceImp(private val customerRepository: CustomerRepository): CustomerService {

    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(customerId: Long): Customer =
        this.customerRepository.findById(customerId)
            .orElseThrow { throw BusinessExcetion("Id $customerId not found") }

    override fun update(customerId: Long, customerUpdateDTO: CustomerUpdateDTO): Customer {
        val customer = this.findById(customerId)
        val updatedCustomer = customerUpdateDTO.toEntity(customer)
        this.customerRepository.save(updatedCustomer)
        return updatedCustomer
    }


    override fun delete(customerId: Long) {
        this.findById(customerId)
        this.customerRepository.deleteById(customerId)
    }
}