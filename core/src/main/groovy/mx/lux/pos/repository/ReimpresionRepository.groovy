package mx.lux.pos.repository

import mx.lux.pos.model.Reimpresion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ReimpresionRepository extends JpaRepository<Reimpresion, Integer>, QueryDslPredicateExecutor<Reimpresion> {
}
