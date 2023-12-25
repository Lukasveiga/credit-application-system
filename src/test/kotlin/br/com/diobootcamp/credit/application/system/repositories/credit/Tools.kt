package br.com.diobootcamp.credit.application.system.repositories.credit

import br.com.diobootcamp.credit.application.system.entities.Address
import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.entities.Customer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

internal class Tools {

    companion object {
        fun buildCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(500.0),
            dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.DECEMBER, 26),
            numberOfInstallments: Int = 5,
            customer: Customer
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallment = numberOfInstallments,
            customer = customer
        )

        fun buildCustomer(
            firstName: String = "Lukas",
            lastName: String = "Veiga",
            cpf: String = "883.799.550-48",
            email: String = "test@email.com",
            password: String = "testPassword",
            zipCode: String = "13545",
            street: String = "Rua Dois Terços",
            income: BigDecimal = BigDecimal(1000.0),
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