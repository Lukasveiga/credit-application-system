package br.com.diobootcamp.credit.application.system.controllers

import br.com.diobootcamp.credit.application.system.dto.credit.CreditDTO
import br.com.diobootcamp.credit.application.system.dto.credit.CreditView
import br.com.diobootcamp.credit.application.system.dto.credit.CreditViewList
import br.com.diobootcamp.credit.application.system.exception.ExceptionDetails
import br.com.diobootcamp.credit.application.system.services.credit.CreditService
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
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

    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Credit Created",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = CreditView::class)))),
        ApiResponse(responseCode = "400", description = "Bad Request",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDTO: CreditDTO): ResponseEntity<CreditView> {
        val credit = this.creditService.save(creditDTO.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreditView(credit))
    }


    @ApiResponse(responseCode = "200", description = "List of credits by customer",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = CreditView::class))]
    )
    @GetMapping("/{customerId}")
    fun findAllByCustomerId(@PathVariable customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList = this.creditService.findAllByCustomer(customerId).map { credit -> CreditViewList(credit) }
        return ResponseEntity.ok(creditViewList)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Credit was found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = CreditView::class)))),
        ApiResponse(responseCode = "404", description = "Credit not found",
            content = arrayOf(Content(mediaType = "application/json", schema = Schema(implementation = ExceptionDetails::class)))
        )
    )
    @GetMapping("/credit/{creditCode}")
    fun findByCreditCode(@RequestParam(value = "customerId") customerId: Long,
                         @PathVariable creditCode: UUID): ResponseEntity<CreditView> {
        val credit = this.creditService.findByCreditCode(customerId, creditCode)
        return ResponseEntity.ok(CreditView(credit))
    }
}