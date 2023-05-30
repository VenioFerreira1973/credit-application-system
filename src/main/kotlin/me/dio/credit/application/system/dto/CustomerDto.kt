package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(

    @field:NotEmpty(message = "O nome é obrigatório") val firstName: String,
    @field:NotEmpty(message = "O sobrenome é obrigatório") val lastName: String,
    @field:CPF(message = "CPF inválido") val cpf: String,
    @field:NotNull(message = "O valor é obrigatório") val income: BigDecimal,
    @field:Email(message = "Email inválido") val email: String,
    @field:NotEmpty(message = "A senha é obrigatória") val password: String,
    @field:NotEmpty(message = "O CEP é obrigatório") val zipCode: String,
    @field:NotEmpty(message = "A rua é obrigatória") val street: String,
    @field:NotNull var id: Long
) {

    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        income = this.income,
        email = this.email,
        password = this.password,
        address = Address(
            zipCode = this.zipCode,
            street = this.street
        ),
        id = this.id

    )
}

