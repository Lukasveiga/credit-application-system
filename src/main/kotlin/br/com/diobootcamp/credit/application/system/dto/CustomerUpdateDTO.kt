package br.com.diobootcamp.credit.application.system.dto

import br.com.diobootcamp.credit.application.system.entities.Customer
import java.math.BigDecimal

data class CustomerUpdateDTO(
    val fristName: String,
    val lastName: String,
    val income: BigDecimal,
    val zipCode: String,
    val street: String
) {
    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.fristName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}
