package mx.lux.pos.repository

import mx.lux.pos.model.Articulo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface ArticuloRepository extends JpaRepository<Articulo, Integer>, QueryDslPredicateExecutor<Articulo> {

  List<Articulo> findByIdIn( Collection<Integer> pId )

  List<Articulo> findByCantExistenciaGreaterThan( Integer pCantExistencia )

  List<Articulo> findByCantExistenciaLessThan( Integer pCantExistencia )
  
}
