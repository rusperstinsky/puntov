package mx.lux.pos.repository

import mx.lux.pos.model.ModificacionCan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ModificacionCanRepository extends JpaRepository<ModificacionCan, Integer>, QueryDslPredicateExecutor<ModificacionCan> {
}
