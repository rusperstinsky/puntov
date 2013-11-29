package mx.lux.pos.repository

import mx.lux.pos.model.CierreDiario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface CierreDiarioRepository extends JpaRepository<CierreDiario, Date>, QueryDslPredicateExecutor<CierreDiario> {

  List<CierreDiario> findByEstadoOrderByFechaAsc( String estado )

  List<CierreDiario> findByFechaBetween( Date fechaInicio, Date fechaFin )

}
