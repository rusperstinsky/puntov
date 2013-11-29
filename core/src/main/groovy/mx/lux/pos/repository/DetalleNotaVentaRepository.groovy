package mx.lux.pos.repository

import mx.lux.pos.model.DetalleNotaVenta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

@Transactional( readOnly = true )
interface DetalleNotaVentaRepository extends JpaRepository<DetalleNotaVenta, Integer>, QueryDslPredicateExecutor<DetalleNotaVenta> {

  DetalleNotaVenta findByIdFacturaAndIdArticulo( String idFactura, Integer idArticulo )

  List<DetalleNotaVenta> findByIdFactura( String idFactura )

  List<DetalleNotaVenta> findByIdFacturaOrderByIdArticuloAsc( String idFactura )

  @Modifying
  @Transactional
  @Query( value = "DELETE FROM detalle_nota_ven WHERE id_factura = ?1", nativeQuery = true )
  void deleteByIdFactura( String pIdFactura )

}
