package mx.lux.pos.repository

import mx.lux.pos.model.Comprobante
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ComprobanteRepository extends JpaRepository<Comprobante, Integer>, QueryDslPredicateExecutor<Comprobante> {

  Comprobante findByIdFiscal( String idFiscal )

  List<Comprobante> findByTicketOrderByFechaImpresionDesc( String ticket )

}
