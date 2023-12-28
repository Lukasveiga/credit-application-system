package br.com.diobootcamp.credit.application.system.controllers.customer

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerDTO
import br.com.diobootcamp.credit.application.system.dto.customer.CustomerUpdateDTO
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import br.com.diobootcamp.credit.application.system.tools.CustomerTools
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerControllerIT {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/v1/customers"
    }

    @BeforeEach
    fun setUp() {
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
    }

    @Test
    fun shouldCreateCustomerAndReturn201StatusCode() {
        // given
        val customerDTO: CustomerDTO = CustomerTools.builderCustomerDTO()
        val customerDTOAsString: String = objectMapper.writeValueAsString(customerDTO)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDTO.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDTO.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customerDTO.cpf))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDTO.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerDTO.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerDTO.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotSaveCustomerWithExistingCPFAndReturn409StatusCode() {
        // given
        customerRepository.save(CustomerTools.builderCustomerDTO().toEntity())
        val customerDTO: CustomerDTO = CustomerTools.builderCustomerDTO()
        val customerDTOAsString: String = objectMapper.writeValueAsString(customerDTO)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request. Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("DataIntegrityViolationException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotSaveCustomerWithFirstNameEmptyAndReturn400StatusCode() {
        // given
        val customerDTO: CustomerDTO = CustomerTools.builderCustomerDTO(firstName = "")
        val customerDTOAsString: String = objectMapper.writeValueAsString(customerDTO)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request. Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.firstName").value("Invalid input"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldFindCustomerByIdAndReturn200StatusCode() {
        // given
        val customerDTO: CustomerDTO = CustomerTools.builderCustomerDTO()
        customerRepository.save(customerDTO.toEntity())
        val customerId: Long = 1L
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$customerId")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDTO.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDTO.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customerDTO.cpf))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDTO.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerDTO.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerDTO.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotFindCustomerByIdWithInvalidIdAndReturn404StatusCode() {
        // given
        val invalidId: Long = 5L
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$invalidId")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Not found exception"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CustomerNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.null").value("Id $invalidId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldDeleteCustomerByIdAndReturn204StatusCode() {
        // given
        val customerSaved: Customer = customerRepository.save(CustomerTools.builderCustomerDTO().toEntity())
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customerSaved.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotDeleteCustomerByIdWithInvalidIdAndReturn404StatusCode() {
        // given
        val invalidId: Long = 5L
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$invalidId")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Not found exception"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CustomerNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.null").value("Id $invalidId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldUpdateCustomerAndReturn200StatusCode() {
        // given
        val customerSaved: Customer = customerRepository.save(CustomerTools.builderCustomerDTO().toEntity())
        val customerUpdateDTO: CustomerUpdateDTO = CustomerTools.builderCustomerUpdateDTO()
        val customerUpdateDTOAsString: String = objectMapper.writeValueAsString(customerUpdateDTO)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=${customerSaved.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerUpdateDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerUpdateDTO.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerUpdateDTO.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerUpdateDTO.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerUpdateDTO.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerUpdateDTO.income))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotUpdateCustomerAndReturn404StatusCode() {
        // given
        val invalidId: Long = Random.nextLong()
        val customerUpdateDTO: CustomerUpdateDTO = CustomerTools.builderCustomerUpdateDTO()
        val customerUpdateDTOAsString: String = objectMapper.writeValueAsString(customerUpdateDTO)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.patch("$URL?customerId=$invalidId")
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerUpdateDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Not found exception"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CustomerNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.null").value("Id $invalidId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

}