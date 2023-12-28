package br.com.diobootcamp.credit.application.system.services.exceptions

class CreditNotFoundException(override val message: String) : RuntimeException(message)
