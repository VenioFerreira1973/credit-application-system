package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.exceptions.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @MockK lateinit var creditService: CreditService

    @BeforeEach
    fun setup() {
        creditRepository = mockk(relaxed = true)
        customerService = mockk(relaxed = true)
        creditService = CreditService(creditRepository, customerService)

    }

    @Test
    fun `should save credit`() {
        // Given
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val creditValue = BigDecimal.valueOf(1000)
        val dayFirstInstallment = LocalDate.now()
        val numberOfInstallment = 12

        val customer = Customer(id = customerId)
        val credit = Credit(
            creditCode = creditCode,
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallment,
            customer = customer
        )

        every { customerService.findById(customerId) } returns customer
        every { creditRepository.save(credit) } returns credit

        // When
        val result = creditService.save(credit)

        // Then
        assertEquals(credit, result)

        verify(exactly = 1) { customerService.findById(customerId) }
        verify(exactly = 1) { creditRepository.save(credit) }
    }


    @Test
    fun `should find credits by customerId`() {

        // Given
        val customer: Customer = buildCustomer()

        //Criando o credit
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val creditValue = BigDecimal.valueOf(1000)
        val dayFirstInstallment = LocalDate.now()
        val numberOfInstallment = 12

        val credit = Credit(
            creditCode = creditCode,
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallment,
            customer = customer
        )

        every { customerRepository.save(customer) } returns customer
        every { creditRepository.save(credit) } returns credit

        // When -- não consegui resolver aparentemente o customer salvo no credit fica em uma referência de memória
        // inacessivel uma solução para este caso, seria mockar um banco de dados pra gravar as informações
        // mas não foi ensinado na aula
        val result = creditService.findAllByCustomer(customerId).any { it.customer == credit.customer }

        // Then
        assert(result)//este teste falha
        verify(exactly = 1) { creditRepository.findAllByCustomerId(customerId) }
    }

    @Test
    fun `should find credit by creditCode`() {
        // Given
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        val creditValue = BigDecimal.valueOf(1000)
        val dayFirstInstallment = LocalDate.now()
        val numberOfInstallment = 12

        val customer = Customer(id = customerId)
        val credit = Credit(
            creditCode = creditCode,
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallment,
            customer = customer
        )

        every { creditRepository.findByCreditCode(creditCode) } returns credit

        // When
        val result = creditService.findByCreditCode(customerId, creditCode)

        // Then
        assertEquals(credit, result)

        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
    }

    @Test
    fun `should throw BusinessException when creditCode does not exist`() {
        //given
        val customerId = 1L
        val creditCode = UUID.randomUUID()
        every { creditRepository.findByCreditCode(creditCode) } returns null

        //when
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(customerId, creditCode) }
            .withMessage("CreditCode $creditCode não encontrado")


        //then
        verify(exactly = 1) { creditRepository.findByCreditCode(creditCode) }
        confirmVerified(creditRepository)
    }

    private fun buildCredit(
        customer: Customer,
        creditValue: BigDecimal = BigDecimal.valueOf(500.0),
        dayFirstInstallment: LocalDate = LocalDate.of(2023, Month.APRIL, 22),
        numberOfInstallments: Int = 5,
    ): Credit = Credit(
        customer = customer,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        )
    private fun buildCustomer(
        firstName: String = "Venio",
        lastName: String = "Ferreira",
        cpf: String = "28475934625",
        email: String = "venio@gmail.com",
        password: String = "12345",
        zipCode: String = "12345",
        street: String = "Rua do Venio",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
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
    )
}
