package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.exceptions.BusinessException
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.ICreditService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.util.*


@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): ICreditService {

    override fun save(credit: Credit): Credit {

        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    /*
    override fun findAllByCustomer(customerId: Long): List<Credit> =
        this.creditRepository.findAllByCustomerId(customerId)*/

    override fun findAllByCustomer(customerId: Long): List<Credit> {

        if(this.creditRepository.findAllByCustomerId(customerId).isEmpty()){
            throw BusinessException("Id: $customerId não encontrado!")
        }else{
            return this.creditRepository.findAllByCustomerId(customerId)
        }
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (this.creditRepository.findByCreditCode(creditCode)
            ?: throw BusinessException("CreditCode $creditCode não encontrado"))

        return if(credit.customer?.id == customerId) credit else throw IllegalArgumentException("Entre em contato com o administrador")
    }




}