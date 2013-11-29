package mx.lux.pos.repository

import mx.lux.pos.model.Empleado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface EmpleadoRepository extends JpaRepository<Empleado, String>, QueryDslPredicateExecutor<Empleado> {

  Empleado findByIdAndPasswd( String id, String password )

  Empleado findById( String id )

}
