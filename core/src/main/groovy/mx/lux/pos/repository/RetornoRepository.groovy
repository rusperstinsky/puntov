package mx.lux.pos.repository

import mx.lux.pos.model.Retorno
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface RetornoRepository extends JpaRepository<Retorno, Integer>, QueryDslPredicateExecutor<Retorno> {
}
