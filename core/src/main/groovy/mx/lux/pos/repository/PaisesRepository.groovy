package mx.lux.pos.repository

import mx.lux.pos.model.Paises
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface PaisesRepository extends JpaRepository<Paises, Integer>, QueryDslPredicateExecutor<Paises> {
}
