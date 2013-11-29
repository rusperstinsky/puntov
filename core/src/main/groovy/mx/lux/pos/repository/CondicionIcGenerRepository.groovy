package mx.lux.pos.repository

import mx.lux.pos.model.CondicionIcGener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface CondicionIcGenerRepository extends JpaRepository<CondicionIcGener, String>, QueryDslPredicateExecutor<CondicionIcGener> {
}
