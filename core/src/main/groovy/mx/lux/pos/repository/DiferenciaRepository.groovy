package mx.lux.pos.repository

import mx.lux.pos.model.Diferencia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DiferenciaRepository extends JpaRepository<Diferencia, Integer>, QueryDslPredicateExecutor<Diferencia> {

}

