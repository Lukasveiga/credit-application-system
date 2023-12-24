package br.com.diobootcamp.credit.application.system.services.customer

import br.com.diobootcamp.credit.application.system.entities.Address
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.Optional
import java.util.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceImpTest {

    private var customerRepository = mockk<CustomerRepository>()

    private var customerServiceImp: CustomerServiceImp = CustomerServiceImp(customerRepository);

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun shouldCreateCustomer() {
        // given
        val customerTest: Customer = buildCustomer()
        every { customerRepository.save(any()) } returns customerTest
        // when
        val actual = customerServiceImp.save(customerTest)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(customerTest)
        verify(exactly = 1) { customerRepository.save(customerTest) }
    }

    @Test
    fun shouldFindCustomerById() {
        //given
        val idTest: Long = Random().nextLong()
        val customerTest: Customer = buildCustomer(id = idTest)
        every { customerRepository.findById(idTest) } returns Optional.of(customerTest)
        //when
        val actual: Customer = customerServiceImp.findById(idTest)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(customerTest)
        verify(exactly = 1) { customerRepository.findById(idTest) }
    }

    private fun buildCustomer(
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