package br.com.diobootcamp.credit.application.system.services.customer

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import br.com.diobootcamp.credit.application.system.services.exceptions.BusinessExcetion
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.Random

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
        val customerTest: Customer = Tools.builderCustomer()
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
        // given
        val idTest: Long = Random().nextLong()
        val customerTest: Customer = Tools.builderCustomer(id = idTest)
        every { customerRepository.findById(idTest) } returns Optional.of(customerTest)
        // when
        val actual: Customer = customerServiceImp.findById(idTest)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(customerTest)
        verify(exactly = 1) { customerRepository.findById(idTest) }
    }

    @Test
    fun shouldNotFindCustomerByIdAndThrowBusinessException() {
        // given
        val idTest: Long = Random().nextLong()
        every { customerRepository.findById(idTest) } returns Optional.empty()
        // when - then
        Assertions.assertThatExceptionOfType(BusinessExcetion::class.java)
            .isThrownBy { customerServiceImp.findById(idTest) }
            .withMessage("Id $idTest not found")
        verify(exactly = 1) { customerRepository.findById(idTest) }
    }

    @Test
    fun shouldUpdateCustomer() {
        // given
        val idTest: Long = Random().nextLong()
        val customerTest: Customer = Tools.builderCustomer()
        val customerUpdateDTOTest: CustomerUpdateDTO = Tools.builderCustomerUpdateDTO()
        every { customerRepository.findById(idTest) } returns Optional.of(customerTest)
        every { customerRepository.save(any()) } returns customerTest
        // When
        val actual: Customer = customerServiceImp.update(idTest, customerUpdateDTOTest)
        // Then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(customerUpdateDTOTest.toEntity(customerTest))
        verify(exactly = 1) { customerRepository.findById(idTest) }
        verify(exactly = 1) { customerRepository.save(customerTest) }
    }
    
    @Test
    fun shouldNotUpdateCostumerAndThrowBusinessException() {
        // given
        val idTest: Long = Random().nextLong()
        val customerUpdateDTOTest: CustomerUpdateDTO = Tools.builderCustomerUpdateDTO()
        every { customerRepository.findById(idTest) } returns Optional.empty()
        // when - then
        Assertions.assertThatExceptionOfType(BusinessExcetion::class.java)
            .isThrownBy { customerServiceImp.update(idTest, customerUpdateDTOTest) }
            .withMessage("Id $idTest not found")
        verify(exactly = 1) { customerRepository.findById(idTest) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun shouldDeleteCustomerById() {
        // given
        val idTest: Long = Random().nextLong()
        val customerTest: Customer = Tools.builderCustomer()
        every { customerRepository.findById(idTest) } returns Optional.of(customerTest)
        every { customerRepository.deleteById(idTest) } just runs
        // when
        customerServiceImp.delete(idTest)
        // then
        verify(exactly = 1) { customerRepository.findById(idTest) }
        verify(exactly = 1) { customerRepository.deleteById(idTest) }
    }

    @Test
    fun shouldNotDeleteCostumerAndThrowBusinessException() {
        // given
        val idTest: Long = Random().nextLong()
        every { customerRepository.findById(idTest) } returns Optional.empty()
        // when - then
        Assertions.assertThatExceptionOfType(BusinessExcetion::class.java)
            .isThrownBy { customerServiceImp.delete(idTest) }
            .withMessage("Id $idTest not found")
        verify(exactly = 1) { customerRepository.findById(idTest) }
        verify(exactly = 0) { customerRepository.deleteById(any()) }
    }
}