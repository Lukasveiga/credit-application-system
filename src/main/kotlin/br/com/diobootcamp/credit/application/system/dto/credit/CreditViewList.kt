package br.com.diobootcamp.credit.application.system.dto.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import java.math.BigDecimal
import java.util.UUID

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallment
    )
}
