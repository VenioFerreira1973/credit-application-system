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
class CreditControllerTest2 {

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
    fun `should not save a credit with numberOfInstallments minor than 1 and return 400 status`() {
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
            //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

        val creditDto: CreditDto = Builds.buildCreditDto(numberOfInstallments = 0)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consulte a documentação"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            ).andExpect(
                MockMvcResultMatchers.jsonPath("$.details[*]").value("O numero de parcelas deve ser maior que 1")
            )
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `should not save a credit with dayFirstInstallment equal actual date and return 400 status`() {
        //given
        val customerDto: CustomerDto = Builds.buildCustomerDto()
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
            // .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())

        val creditDto: CreditDto = Builds.buildCreditDto(dayFirstInstallment = LocalDate.now())
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consulte a documentação"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            ).andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("A data informa é inválida"))
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun `find a credit with valid customer id and return 200 status`() {
        //given
        val customerDto: CustomerDto = Builds.buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditControllerTest.URL2)
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

        val creditDto: CreditDto = Builds.buildCreditDto()
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post(CreditControllerTest.URL1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit)
        )

            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("1000.0"))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.dayFirstInstallment").value(LocalDate.now().plusDays(10).toString())
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(12))
            //.andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1))
            .andDo(MockMvcResultHandlers.print())


        val validId = 1

        mockMvc.perform(
            MockMvcRequestBuilders.get(CreditControllerTest.URL1)
                .accept(MediaType.APPLICATION_JSON)
                .param("customerId", validId.toString())
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())


    }


}
