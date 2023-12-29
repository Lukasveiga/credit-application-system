package br.com.diobootcamp.credit.application.system.tools

import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.entities.Customer
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

internal class CreditTools {

    companion object {
        fun builderCredit(
            creditValue: BigDecimal = BigDecimal.valueOf(500.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusDays(1),
            numberOfInstallments: Int = 5,
            customer: Customer
        ): Credit = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallment = numberOfInstallments,
            customer = customer
        )

        fun builderCreditDTO(
            creditValue: BigDecimal = BigDecimal.valueOf(500.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusDays(1),
            numberOfInstallments: Int = 5,
            customerId: Long
        ): CreditDTO = CreditDTO(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            customerId = customerId
        )
    }

}