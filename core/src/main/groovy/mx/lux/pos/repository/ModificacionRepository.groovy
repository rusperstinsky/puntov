package mx.lux.pos.repository

import mx.lux.pos.model.Modificacion
import mx.lux.pos.repository.custom.ModificacionRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ModificacionRepository extends JpaRepository<Modificacion, Integer>, QueryDslPredicateExecutor<Modificacion>, ModificacionRepositoryCustom {

  List<Modificacion> findByFechaBetween( Date fechaInicio, Date fechaFin )

  List<Modificacion> findByFechaBetweenAndIdFactura( Date fechaInicio, Date fechaFin, String idFactura )

  List<Modificacion> findByIdFacturaOrderByFechaAsc( String idFactura )

  List<Modificacion> findByIdFacturaAndTipo( String idFactura, String tipo )

  List<Modificacion> findByIdFactura( String pIdFactura )

}
