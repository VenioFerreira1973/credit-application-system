package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.junit5.MockKExtension
import me.dio.credit.application.system.dto.CreditDto
import me.dio.credit.application.system.dto.CustomerDto
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
@ExtendWith(MockKExtension::class)
class CreditControllerTest3 {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()

    }


    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()

    }

    companion object {
        const val URL1: String = "/api/credits"
        const val URL2: String = "/api/customers"

    }

    @Test
    fun `find a credit with invalid creditCode and return 404 status`() {
        //given
        val customerIdFake = 1L
        val customerDto: CustomerDto = Builds.buildCustomerDto(id = customerIdFake)
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Venio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Ferreira"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("100.290.370-00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("venio@email.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value("1000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value("000000"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("Rua do Venio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

        val creditCodeFake = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        val creditCodeFakeInvalid = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a30")

        val creditDto: CreditDto = Builds.buildCreditDto(
            creditCode = creditCodeFake,
            customerId = customerIdFake,
            id = 1
        )
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.dayFirstInstallment")
                    .value(LocalDate.now().plusDays(10).toString())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
            .andDo(MockMvcResultHandlers.print())

        mockMvc.perform(
            MockMvcRequestBuilders.get("${CustomerResourceTest.URL}/${creditCodeFakeInvalid}")
                .accept(MediaType.APPLICATION_JSON)
                .param("creditCode", creditCodeFake.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andDo(MockMvcResultHandlers.print())

    }

}