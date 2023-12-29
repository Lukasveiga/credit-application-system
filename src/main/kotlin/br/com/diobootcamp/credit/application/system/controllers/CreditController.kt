package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.dto.credit.CreditView
import br.com.diobootcamp.credit.application.system.dto.credit.CreditViewList
import br.com.diobootcamp.credit.application.system.services.credit.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/v1/credits")
class CreditController(private val creditService: CreditService) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDTO: CreditDTO): ResponseEntity<CreditView> {
        val credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreditView(credit))
    }

    @GetMapping("/{customerId}")
    fun findAllByCustomerId(@PathVariable customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).map { credit -> CreditViewList(credit) }
        return ResponseEntity.ok(creditViewList)
    }

    @GetMapping("/credit/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
        val credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.ok(CreditView(credit))
    }
}