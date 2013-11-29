package mx.lux.pos.repository

import mx.lux.pos.model.RetornoDet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface RetornoDetRepository extends JpaRepository<RetornoDet, Integer>, QueryDslPredicateExecutor<RetornoDet> {
}
