package mx.lux.pos.repository

import mx.lux.pos.model.Devolucion
import mx.lux.pos.repository.custom.DevolucionRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DevolucionRepository extends JpaRepository<Devolucion, Integer>, QueryDslPredicateExecutor<Devolucion>, DevolucionRepositoryCustom {

  List<Devolucion> findByFechaBetween( Date fechaInicio, Date fechaFin )

  List<Devolucion> findByFechaBetweenAndTipo( Date fechaInicio, Date fechaFin, String tipo )

  List<Devolucion> findByIdModInOrderByFechaAsc( List<Integer> idMods )

  List<Devolucion> findByIdModOrderByFechaAsc( Integer idMod )

  List<Devolucion> findByIdPago( Integer idPago )

}
