package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(

    @field:NotNull(message = "O valor é obrigatório") val creditValue: BigDecimal,
    @field:Future(message = "A data informa é inválida") val dayFirstInstallment: LocalDate,
    @field:Min(value = 1, message = "O numero de parcelas deve ser maior que 1") @field:Max(
        value = 12,
        message = "O numero de parcelas deve ser menor que 12"
    ) val numberOfInstallment: Int,
    @field:NotNull(message = "O id do cliente é obrigatório") var customerId: Long
) {

    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallment = this.numberOfInstallment,
        customer = Customer(
            id = this.customerId
        )
    )

}
