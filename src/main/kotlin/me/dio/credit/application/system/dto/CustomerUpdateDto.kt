package me.dio.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "O nome é obrigatório") val firstName: String,
    @field:NotEmpty(message = "O sobrenome é obrigatório") val lastName: String,
    @field:NotNull(message = "O valor é obrigatório") val income: BigDecimal,
    @field:NotEmpty(message = "O CEP é obrigatório") val zipCode: String,
    @field:NotEmpty(message = "A rua é obrigatória") val street: String
) {

    fun toEntity(customer: Customer): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street

        return customer
    }


}
