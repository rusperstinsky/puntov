package mx.lux.pos.repository

import mx.lux.pos.model.Precio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface PrecioRepository extends JpaRepository<Precio, Integer>, QueryDslPredicateExecutor<Precio> {

  Precio findByArticuloAndLista( String articulo, String lista )

  List<Precio> findByArticulo( String articulo )

}
