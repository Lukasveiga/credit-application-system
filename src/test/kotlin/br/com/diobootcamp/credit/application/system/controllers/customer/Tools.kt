package br.com.diobootcamp.credit.application.system.controllers.customer

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerDTO
import br.com.diobootcamp.credit.application.system.dto.customer.CustomerUpdateDTO
import java.math.BigDecimal

internal class Tools {

    companion object {

        fun builderCustomerDTO(
            firstName: String = "Lukas",
            lastName: String = "Veiga",
            cpf: String = "883.799.550-48",
            email: String = "test@email.com",
            password: String = "testPassword",
            zipCode: String = "13545",
            street: String = "Rua Dois Terços",
            income: BigDecimal = BigDecimal(0)
        ): CustomerDTO = CustomerDTO(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            zipCode = zipCode,
            street = street,
            income = income,
        )

        fun builderCustomerUpdateDTO(
            firstName: String = "Lukas",
            lastName: String = "Veiga",
            zipCode: String = "13545",
            street: String = "Rua Dois Terços",
            income: BigDecimal = BigDecimal(0)
        ): CustomerUpdateDTO = CustomerUpdateDTO(
            firstName = firstName,
            lastName = lastName,
            zipCode = zipCode,
            street = street,
            income = income,
        )
    }
}