package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.customer.CustomerDTO
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(1))
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
}