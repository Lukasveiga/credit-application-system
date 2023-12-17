package br.com.diobootcamp.credit.application.system.repositories

import br.com.diobootcamp.credit.application.system.entities.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, Long>