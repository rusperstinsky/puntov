package mx.lux.pos.repository

import mx.lux.pos.model.ModificacionEmp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ModificacionEmpRepository extends JpaRepository<ModificacionEmp, Integer>, QueryDslPredicateExecutor<ModificacionEmp> {
}
