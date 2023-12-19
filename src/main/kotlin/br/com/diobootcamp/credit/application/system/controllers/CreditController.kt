package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.dto.credit.CreditViewList
import br.com.diobootcamp.credit.application.system.services.credit.CreditService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/credits")
class CreditController(private val creditService: CreditService) {

    @PostMapping
    fun saveCredit(@RequestBody creditDTO: CreditDTO): ResponseEntity<String> {
        val credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body("Credit ${credit.creditCode} - Customer ${credit.customer?.firstName} saved")
    }

    @GetMapping("/{customerId}")
    fun findAllByCustomerId(@RequestParam customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).map { credit -> CreditViewList(credit) }
        return ResponseEntity.ok(creditViewList)
    }
}