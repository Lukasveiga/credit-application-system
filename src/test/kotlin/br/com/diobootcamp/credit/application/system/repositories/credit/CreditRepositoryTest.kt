package br.com.diobootcamp.credit.application.system.repositories.credit

import br.com.diobootcamp.credit.application.system.entities.Credit
import br.com.diobootcamp.credit.application.system.entities.Customer
import br.com.diobootcamp.credit.application.system.repositories.CreditRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired lateinit var creditRepository: CreditRepository

    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach
    fun setUp() {
        customer = testEntityManager.persist(Tools.buildCustomer())
        credit1 = testEntityManager.persist(Tools.buildCredit(customer = customer))
        credit2 = testEntityManager.persist(Tools.buildCredit(customer = customer))
    }

    @Test
    fun shouldFindCreditByCreditCode() {
        // when
        val credit1Test: Credit = creditRepository.findByCreditCode(credit1.creditCode)!!
        val credit2Test: Credit = creditRepository.findByCreditCode(credit2.creditCode)!!
        // then
        Assertions.assertThat(credit1Test).isNotNull
        Assertions.assertThat(credit2Test).isNotNull

        Assertions.assertThat(credit1Test).isSameAs(credit1)
        Assertions.assertThat(credit2Test).isSameAs(credit2)
    }
}