package br.com.diobootcamp.credit.application.system.services.exceptions

data class BusinessExcetion(override val message: String) : RuntimeException(message)
