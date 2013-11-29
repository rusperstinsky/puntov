package mx.lux.pos.repository

import mx.lux.pos.model.Cotizacion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor
import org.springframework.data.jpa.repository.Query

interface CotizacionRepository extends JpaRepository<Cotizacion, Integer>,
    QueryDslPredicateExecutor<Cotizacion> {

  @Query( value = "SELECT NEXTVAL('cotiza_id_cotiza_seq')", nativeQuery = true )
  BigInteger nextIdCotiza( )

}
