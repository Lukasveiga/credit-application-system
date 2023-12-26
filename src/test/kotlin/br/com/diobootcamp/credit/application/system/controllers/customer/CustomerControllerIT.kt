package br.com.diobootcamp.credit.application.system.controllers.customer

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerDTO
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
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
        val customerDTO: CustomerDTO = Tools.buildCustomerDTO()
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
    fun shouldNotSaveCustomerWithCPFAndReturn409StatusCode() {
        // given
        customerRepository.save(Tools.buildCustomerDTO().toEntity())
        val customerDTO: CustomerDTO = Tools.buildCustomerDTO()
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
        val customerDTO: CustomerDTO = Tools.buildCustomerDTO(firstName = "")
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
        val customerDTO: CustomerDTO = Tools.buildCustomerDTO()
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
    fun shouldNotFindCustomerByIdWithInvalidIdAndReturn400StatusCode() {
        // given
        val customerId: Long = 5L
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$customerId")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request. Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("BusinessExcetion"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.null").value("Id $customerId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldDeleteCustomerByIdAndReturn204StatusCode() {
        // given
        val customerSaved: Customer = customerRepository.save(Tools.buildCustomerDTO().toEntity())
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customerSaved.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotDeleteCustomerByIdWithInvalidIdAndReturn400StatusCode() {
        // given
        val customerId: Long = 5L
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$customerId")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request. Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("BusinessExcetion"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details.null").value("Id $customerId not found"))
            .andDo(MockMvcResultHandlers.print())
    }
}