package mx.lux.pos.repository

import mx.lux.pos.model.Localidad
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface LocalidadRepository extends JpaRepository<Localidad, Integer>, QueryDslPredicateExecutor<Localidad> {

  List<Localidad> findByMunicipioEstadoNombreAndMunicipioNombreOrderByUsuarioAsc( String estado, String municipio )

  List<Localidad> findByCodigoAndUsuario( String codigo, String usuario )

  List<Localidad> findByCodigo( String codigo )

}
