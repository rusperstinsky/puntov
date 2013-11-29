package mx.lux.pos.repository

import mx.lux.pos.model.PagoExterno
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface PagoExternoRepository extends JpaRepository<PagoExterno, Integer>, QueryDslPredicateExecutor<PagoExterno> {

  List<PagoExterno> findByFechaGreaterThanAndFechaLessThanAndFormaPago_aceptaEnPagos( Date fechaInicio, Date fechaFin, Boolean aceptaEnPagos )

  List<PagoExterno> findByFechaBetween( Date fechaInicio, Date fechaFin )

  List<PagoExterno> findByFechaBetweenAndIdTerminalIn( Date fechaInicio, Date fechaFin, Collection<String> idTerminal )

}
