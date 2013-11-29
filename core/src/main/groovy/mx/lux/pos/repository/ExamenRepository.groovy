package mx.lux.pos.repository

import mx.lux.pos.model.Examen
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ExamenRepository extends JpaRepository<Examen, Integer>, QueryDslPredicateExecutor<Examen> {
}
