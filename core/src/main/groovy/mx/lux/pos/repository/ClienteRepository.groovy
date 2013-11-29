package mx.lux.pos.repository

import mx.lux.pos.model.Cliente
import mx.lux.pos.repository.custom.ClienteRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ClienteRepository extends JpaRepository<Cliente, Integer>, QueryDslPredicateExecutor<Cliente>, ClienteRepositoryCustom {

}
