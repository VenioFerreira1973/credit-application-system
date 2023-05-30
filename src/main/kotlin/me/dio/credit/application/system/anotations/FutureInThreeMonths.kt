package me.dio.credit.application.system.anotations

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.time.LocalDate
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FutureInThreeMonthsValidator::class])
annotation  class FutureInThreeMonths(
    val message: String = "A data informada deve ser exatamente 3 meses no futuro",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
class FutureInThreeMonthsValidator : ConstraintValidator<FutureInThreeMonths, LocalDate> {
    override fun isValid(value: LocalDate?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return true // Permite valores nulos, a validação será realizada pela anotação @NotNull
        }

        val currentDate = LocalDate.now()
        val futureDate = currentDate.plusMonths(3) // Adiciona 3 meses à data atual

        return value == futureDate
    }
}