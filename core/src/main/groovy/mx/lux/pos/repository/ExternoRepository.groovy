package mx.lux.pos.repository

import mx.lux.pos.model.Externo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ExternoRepository extends JpaRepository<Externo, Integer>, QueryDslPredicateExecutor<Externo> {
}
