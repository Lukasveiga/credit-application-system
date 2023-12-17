package br.com.diobootcamp.credit.application.system.repositories

import br.com.diobootcamp.credit.application.system.entities.Credit
import org.springframework.data.jpa.repository.JpaRepository

interface CreditRepository: JpaRepository<Credit, Long>