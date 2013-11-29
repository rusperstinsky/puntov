package mx.lux.pos.repository

import mx.lux.pos.model.Rep
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface RepRepository extends JpaRepository<Rep, String>, QueryDslPredicateExecutor<Rep> {
}
