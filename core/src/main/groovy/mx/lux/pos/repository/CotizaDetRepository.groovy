package mx.lux.pos.repository

import mx.lux.pos.model.CotizaDet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface CotizaDetRepository extends JpaRepository<CotizaDet, Integer>, QueryDslPredicateExecutor<CotizaDet> {

   List<CotizaDet> findByIdCotiza(Integer pIdCotiza)

}
