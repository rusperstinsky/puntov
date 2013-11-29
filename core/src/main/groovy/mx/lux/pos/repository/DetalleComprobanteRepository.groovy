package mx.lux.pos.repository

import mx.lux.pos.model.DetalleComprobante
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Integer>, QueryDslPredicateExecutor<DetalleComprobante> {

  List<DetalleComprobante> findByIdFiscalOrderByIdArticuloAsc( String idFiscal )

}
