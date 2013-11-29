package mx.lux.pos.repository

import mx.lux.pos.model.Sucursal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface SucursalRepository extends JpaRepository<Sucursal, Integer>, QueryDslPredicateExecutor<Sucursal> {

  @Query( value = "SELECT esta_sucursal()", nativeQuery = true )
  Integer getCurrentSucursalId( )

}
