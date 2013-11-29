package mx.lux.pos.repository

import mx.lux.pos.model.OrdenProm
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

@Transactional( readOnly = true )
interface OrdenPromRepository extends JpaRepository<OrdenProm, Integer>,
    QueryDslPredicateExecutor<OrdenProm> {

  List<OrdenProm> findByIdPromocion( Integer pIdPromocion )

  List<OrdenProm> findByIdFactura( String pIdFactura )

  @Modifying
  @Transactional
  @Query( value = 'DELETE FROM orden_prom WHERE id_factura = ?1', nativeQuery = true )
  void deleteByIdFactura( String pIdFactura )

}
