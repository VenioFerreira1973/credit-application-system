package me.dio.credit.application.system.controller

import me.dio.credit.application.system.dto.CreditDto
import me.dio.credit.application.system.dto.CustomerDto
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class Builds {
    companion object {

        fun buildCreditDto(
            creditValue: BigDecimal = BigDecimal.valueOf(1000.0),
            dayFirstInstallment: LocalDate = LocalDate.now().plusDays(10),
            numberOfInstallments: Int = 12,
            customerId: Long = 1L,
            id: Long = 1L,
            creditCode: UUID = UUID.fromString("aa547c0f-9a6a-451f-8c89-afddce916a29")
        ): CreditDto {
            return CreditDto(
                creditValue = creditValue,
                dayFirstInstallment = dayFirstInstallment,
                numberOfInstallments = numberOfInstallments,
                customerId = customerId,
                id = id,
                creditCode = creditCode
            )
        }


        fun buildCustomerDto(
            firstName: String = "Venio",
            lastName: String = "Ferreira",
            cpf: String = "100.290.370-00",
            email: String = "venio@email.com",
            income: BigDecimal = BigDecimal.valueOf(1000.0),
            password: String = "1234",
            zipCode: String = "000000",
            street: String = "Rua do Venio",
            id: Long = 1L
        ) = CustomerDto(
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            income = income,
            password = password,
            zipCode = zipCode,
            street = street,
            id = id
        )
    }
}