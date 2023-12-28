package br.com.diobootcamp.credit.application.system.services.exceptions

class CustomerNotFoundException(override val message: String) : RuntimeException(message)
