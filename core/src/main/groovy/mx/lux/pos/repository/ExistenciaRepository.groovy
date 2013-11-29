package mx.lux.pos.repository

import mx.lux.pos.model.Existencia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ExistenciaRepository extends JpaRepository<Existencia, Integer>, QueryDslPredicateExecutor<Existencia> {
}
