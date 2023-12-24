package br.com.diobootcamp.credit.application.system.services.customer

import br.com.diobootcamp.credit.application.system.entities.Address
import br.com.diobootcamp.credit.application.system.entities.Customer
import java.math.BigDecimal

internal class Tools {

    companion object {
        fun buildCustomer(
        firstName: String = "Lukas",
        lastName: String = "Veiga",
        cpf: String = "883.799.550-48",
        email: String = "test@email.com",
        password: String = "testPassword",
        zipCode: String = "13545",
        street: String = "Rua Dois Terços",
        income: BigDecimal = BigDecimal(0),
        id: Long = 1
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        ),
        income = income,
    )
    }
}