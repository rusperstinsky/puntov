package mx.lux.pos.repository

import mx.lux.pos.model.Contribuyente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ContribuyenteRepository extends JpaRepository<Contribuyente, Integer>, QueryDslPredicateExecutor<Contribuyente> {

  List<Contribuyente> findByRfcOrderByFechaRegistroDesc( String rfc )

  List<Contribuyente> findByRfcStartingWithOrderByRfcAsc( String rfc )

  List<Contribuyente> findByIdCliente( Integer pIdCliente )

  List<Contribuyente> findByIdClienteAndRfc( Integer pIdCliente, String pRfc )

}
