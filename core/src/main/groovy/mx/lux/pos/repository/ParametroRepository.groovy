package mx.lux.pos.repository

import mx.lux.pos.model.Parametro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ParametroRepository extends JpaRepository<Parametro, String>, QueryDslPredicateExecutor<Parametro> {
}
