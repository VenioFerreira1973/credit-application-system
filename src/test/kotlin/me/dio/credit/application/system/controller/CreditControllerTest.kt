package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CreditDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.math.BigDecimal
import java.time.LocalDate
import io.mockk.*
import org.springframework.boot.test.mock.mockito.MockBean
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.service.impl.CustomerService
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var customerService: CustomerService

    @MockBean
    private lateinit var creditController: CreditController

    @BeforeEach
    fun setup() {
        every { customerService.equals(any()) } returns true
    }

    companion object {
        const val URL1: String = "/api/costumers"
        const val URL2: String = "/api/credits"
    }


    @Test
    fun `should create a credit and return 201 status`() {
        // Given
        val customer = Customer(
            firstName = "Venio",
            lastName = "Ferreira",
            cpf = "631.069.980-67",
            email = "venio@gmail.com",
            password = "12345",
            address = Address(
                zipCode = "12345",
                street = "Rua da Vida",
            ),
            income = BigDecimal.valueOf(1000.0),
            id = 1L
        )

        val creditDto = CreditDto(
            creditValue = BigDecimal.valueOf(1000),
            dayFirstInstallment = LocalDate.now().plusDays(10),
            numberOfInstallments = 12,
            customerId = 1L
        )

        val fakeCredit = creditController.saveCredit(creditDto)

        // When/Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL2)
                .contentType("application/json")
                .content(creditDto.toJson())
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    private fun buildCustomer(
        firstName: String = "Venio",
        lastName: String = "Ferreira",
        cpf: String = "631.069.980-67",
        email: String = "venio@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua da Vida",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )


}