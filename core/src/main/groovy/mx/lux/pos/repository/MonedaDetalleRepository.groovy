package mx.lux.pos.repository

import org.springframework.data.jpa.repository.JpaRepository

import mx.lux.pos.model.MonedaDetalle
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface MonedaDetalleRepository extends JpaRepository<MonedaDetalle, Integer>, QueryDslPredicateExecutor<MonedaDetalle> {

  List<MonedaDetalle> findByIdMoneda( String pIdMoneda )

  List<MonedaDetalle> findByIdMonedaAndFechaActiva( String pIdMoneda, Date pActiveBefore )

  List<MonedaDetalle> findByIdMonedaAndFechaActivaBefore( String pIdMoneda, Date pActiveBefore )

}
