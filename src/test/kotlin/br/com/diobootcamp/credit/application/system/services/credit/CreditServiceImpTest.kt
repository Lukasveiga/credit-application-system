package br.com.diobootcamp.credit.application.system.services.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
import br.com.diobootcamp.credit.application.system.tools.CreditTools
import br.com.diobootcamp.credit.application.system.tools.CustomerTools
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreditServiceImpTest {

    private val creditRepository = mockk<CreditRepository>()

    private val customerService: CustomerService = mockk<CustomerService>()

    private val creditServiceImp: CreditServiceImp = CreditServiceImp(creditRepository, customerService)

    @Test
    fun shouldCreateCredit() {
        // given
        val customerTest: Customer = CustomerTools.builderCustomer(id = 1)
        val creditTest: Credit = CreditTools.builderCredit(customer = customerTest)
        every { customerService.findById(1) } returns customerTest
        every { creditRepository.save(any()) } returns creditTest
        // when
        val creditSaved: Credit = creditServiceImp.save(creditTest)
        // then
        Assertions.assertThat(creditSaved).isNotNull
    }
}