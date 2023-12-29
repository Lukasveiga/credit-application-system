package br.com.diobootcamp.credit.application.system.services.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import br.com.diobootcamp.credit.application.system.services.customer.CustomerService
import br.com.diobootcamp.credit.application.system.services.exceptions.InvalidDayFirstOfInstallmentException
import br.com.diobootcamp.credit.application.system.tools.CreditTools
import br.com.diobootcamp.credit.application.system.tools.CustomerTools
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

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

    @Test
    fun shouldNotCreateCreditWithInvalidDayFirstInstallment() {
        // given
        val customerTest: Customer = CustomerTools.builderCustomer(id = 1)
        val creditTest: Credit = CreditTools.builderCredit(customer = customerTest, dayFirstInstallment = LocalDate.now().plusMonths(3))
        // when - then
        Assertions.assertThatExceptionOfType(InvalidDayFirstOfInstallmentException::class.java)
            .isThrownBy { creditServiceImp.save(creditTest) }
            .withMessage("Day first of installment has to be maximum three months ahead")
        verify(exactly = 0) { creditRepository.save(creditTest) }
    }

    @Test
    fun shouldFindAllCreditsByCustomer() {
        // given
        val customerTest: Customer = CustomerTools.builderCustomer(id = 1)
        val creditTest: Credit = CreditTools.builderCredit(customer = customerTest)
        every { creditRepository.findAllByCustomerId(any()) } returns mutableListOf(creditTest)
        // when
        val creditList: List<Credit> = creditServiceImp.findAllByCustomer(customerTest.id!!)
        // then
        Assertions.assertThat(creditList).isNotEmpty
        Assertions.assertThat(creditList.size).isEqualTo(1)
        Assertions.assertThat(creditList[0]).isSameAs(creditTest)

    }
}