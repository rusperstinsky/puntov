package mx.lux.pos.repository

import mx.lux.pos.model.Municipio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface MunicipioRepository extends JpaRepository<Municipio, Integer>, QueryDslPredicateExecutor<Municipio> {

  List<Municipio> findByEstadoNombreOrderByNombreAsc( String estado )

  Municipio findByEstadoNombreAndNombre( String estado, String nombre )

}
