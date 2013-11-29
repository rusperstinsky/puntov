package mx.lux.pos.repository

import mx.lux.pos.model.EntregadoExterno
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface EntregadoExternoRepository extends JpaRepository<EntregadoExterno, String>, QueryDslPredicateExecutor<EntregadoExterno> {

  List<EntregadoExterno> findByFechaGreaterThanAndFechaLessThan( Date fechaInicio, Date fechaFin );

  List<EntregadoExterno> findByIdFactura( String idFactura )

}
