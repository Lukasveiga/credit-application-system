package br.com.diobootcamp.credit.application.system.controllers.credit

import br.com.diobootcamp.credit.application.system.controllers.IntegrationTestConfig
import br.com.diobootcamp.credit.application.system.controllers.customer.CustomerControllerIT
import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.entities.Credit
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
import java.util.UUID

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
        // when - then
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
        // when - then
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

    @Test
    fun shouldFindAllCreditsByCustomerId() {
        // given
        val creditDTO: CreditDTO = CreditTools.builderCreditDTO(customerId = 1)
        creditRepository.save(creditDTO.toEntity())
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${1}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(creditDTO.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(creditDTO.numberOfInstallments))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotFindAllCreditsByCustomerId() {
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${1}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldFindCreditByCreditCode() {
        // given
        val credit: Credit = CreditTools.builderCreditDTO(customerId = 1).toEntity()
        creditRepository.save(credit)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/credit/${credit.creditCode}?customerId=${1}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(credit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(credit.numberOfInstallment))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customerTest.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customerTest.income.longValueExact()))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotFindCreditByCreditCodeWithInvalidCreditCode() {
        // given
        val invalidCreditCode: UUID = UUID.randomUUID()
        val credit: Credit = CreditTools.builderCreditDTO(customerId = 1).toEntity()
        creditRepository.save(credit)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/credit/${invalidCreditCode}?customerId=${1}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Not found exception"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CreditNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun shouldNotFindCreditByCreditCodeWithInvalidCustomerId() {
        // given
        val credit: Credit = CreditTools.builderCreditDTO(customerId = 1).toEntity()
        creditRepository.save(credit)
        // when - then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/credit/${credit.creditCode}?customerId=${5}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Not found exception"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception").value("CreditNotFoundException"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }
}