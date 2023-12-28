package br.com.diobootcamp.credit.application.system.services.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
import br.com.diobootcamp.credit.application.system.services.exceptions.BusinessExcetion
import br.com.diobootcamp.credit.application.system.services.exceptions.CreditNotFoundException
import br.com.diobootcamp.credit.application.system.services.exceptions.InvalidDayFirstOfInstallmentException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class CreditServiceImp(private val creditRepository: CreditRepository, private val customerService: CustomerService): CreditService {
    override fun save(credit: Credit): Credit {
        this.validateDayFirstOfInstallment(credit.dayFirstInstallment)
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = this.creditRepository.findByCreditCode(creditCode) ?: throw CreditNotFoundException("Credit code $creditCode not found")

        return if (credit.customer?.id == customerId) credit else throw CreditNotFoundException("Credit code $creditCode not found")
    }

    private fun validateDayFirstOfInstallment(dayFirstOfInstallment: LocalDate): Boolean {
        return if (dayFirstOfInstallment.isBefore(LocalDate.now().plusMonths(3))) true
        else throw InvalidDayFirstOfInstallmentException("Day first of installment has to be at least three months ahead")
    }
}