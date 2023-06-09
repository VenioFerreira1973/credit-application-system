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
class CreditControllerTest {

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
    fun `should create a credit and return 201 status`() {
        //given
        //val customerIdFake = 1L
        val customerDto: CustomerDto = Builds.buildCustomerDto()//(id = customerIdFake)
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
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

        val creditDto: CreditDto = Builds.buildCreditDto(customerId = 1L)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").value(LocalDate.now().plusDays(10).toString())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(12))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
            .andDo(MockMvcResultHandlers.print())


    }

    @Test
    fun `should not save a credit with customer invalid and return 404 status`() {
        //given
        val creditDto: CreditDto = Builds.buildCreditDto(customerId = 2L)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("NOT FOUND!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exceptions.BusinessException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.details[*]").value("Id: ${creditDto.customerId} não encontrado!")
            )
            .andDo(MockMvcResultHandlers.print())

    }


    @Test
    fun `should not find credit whith invalid customer id and return 400 status`() {
        //given
        val invalidId = 2L
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL1)
                .accept(MediaType.APPLICATION_JSON)
                .param("customerId", invalidId.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("NOT FOUND!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exceptions.BusinessException")

            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }


}