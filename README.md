# DIO Bootcamp: Desenvolvimento Backend com Kotlin - API Credit Application System

<p align="center">
     <a alt="Java">
        <img src="https://img.shields.io/badge/Java-v17-blue.svg" />
    </a>
    <a alt="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-v1.7.22-purple.svg" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring%20Boot-v3.0.3-brightgreen.svg" />
    </a>
    <a alt="Gradle">
        <img src="https://img.shields.io/badge/Apache Maven-3.8.6-lightgreen.svg" />
    </a>
    <a alt="H2 ">
        <img src="https://img.shields.io/badge/H2-v2.1.214-darkblue.svg" />
    </a>
    <a alt="Flyway">
        <img src="https://img.shields.io/badge/Flyway-v9.5.1-red.svg">
    </a>
    <a alt="PostgreSQL">
        <img src="https://img.shields.io/badge/PostgreSQL-14.alpine-black.svg">
    </a>
</p>

## Rest Api desenvolvida no bootcamp da DIO utilizando Kotlin, Spring Boot e PostgreSQL.

### Conteúdo abordado:
<ul>
    <li>Kotlin</li>
    <li>Banco de dados relacional (H2 e PostgreSQL)</li>
    <li>ORM Hibernate</li>
    <li>Spring Validation</li>
    <li>Testes Unitários e de Integração com JUnit, Assertj e Mockk</li>
    <li>Tratamento de Exceções</li>
    <li>Documentação com Swagger e OpenAPI 3</li>
</ul>

### Script de testes: 

Testes Unitários
```
mvn test -Punit-test
```

Testes de Integração:
```
mvn test -Pintegration-test
```

### Configurações de ambiente:

.env

```
DB_NAME=credit-application-system-db
DB_USER=username
DB_PASS=password
DB_PORT=5433
```

### Docker-compose PostgreSQL:

Subir container do banco de dados:

```
docker-compose up --build
```

Parar container docker:

```
docker-compose down
```

### Documentação: http://localhost:8080/swagger-ui/index.html

<hr>

#### Autor:

Lukas Veiga

[![Linkedin Badge](https://img.shields.io/badge/-LukasVeiga-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/lukas-veiga/)](https://www.linkedin.com/in/lukas-veiga/)




