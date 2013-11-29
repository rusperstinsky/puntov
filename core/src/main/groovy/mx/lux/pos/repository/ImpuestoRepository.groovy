package mx.lux.pos.repository;

import mx.lux.pos.model.Impuesto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ImpuestoRepository extends JpaRepository<Impuesto, String>, QueryDslPredicateExecutor<Impuesto> {
}
