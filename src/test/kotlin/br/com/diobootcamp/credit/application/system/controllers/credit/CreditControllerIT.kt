package br.com.diobootcamp.credit.application.system.controllers.credit

import br.com.diobootcamp.credit.application.system.controllers.IntegrationTestConfig
import br.com.diobootcamp.credit.application.system.controllers.customer.CustomerControllerIT
import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import br.com.diobootcamp.credit.application.system.repositories.CustomerRepository
import br.com.diobootcamp.credit.application.system.tools.CreditTools
import br.com.diobootcamp.credit.application.system.tools.CustomerTools
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class CreditControllerIT: IntegrationTestConfig() {

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/v1/credits"
        val customerTest: Customer = CustomerTools.builderCustomer(id = null)
    }

    @BeforeEach
    fun setUp() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
        customerRepository.save(customerTest)
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun shouldCreateCreditAndReturn201StatusCode() {
        // given
        val creditDTO: CreditDTO = CreditTools.builderCreditDTO(customerId = 1)
        val creditDTOAsString: String = objectMapper.writeValueAsString(creditDTO)
        // when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(creditDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDTO.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(creditDTO.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customerTest.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customerTest.income.longValueExact()))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotCreateCreditAndReturn404StatusCodeWhenInvalidNumberOfInstallmentsIsProvided() {
        // given
        val creditDTO: CreditDTO = CreditTools.builderCreditDTO(customerId = 1, numberOfInstallments = 49)
        val creditDTOAsString: String = objectMapper.writeValueAsString(creditDTO)
        // when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDTOAsString))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request. Consult documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("MethodArgumentNotValidException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }
}