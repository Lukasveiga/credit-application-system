package br.com.diobootcamp.credit.application.system.services.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
import br.com.diobootcamp.credit.application.system.services.exceptions.BusinessExcetion
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditServiceImp(private val creditRepository: CreditRepository, private val customerService: CustomerService): CreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }

        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> {
        return this.creditRepository.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = this.creditRepository.findByCreditCode(creditCode) ?: throw BusinessExcetion("Credit code $creditCode not found")

        return if (credit.customer?.id == customerId) credit else throw BusinessExcetion("Contact admin")
    }
}