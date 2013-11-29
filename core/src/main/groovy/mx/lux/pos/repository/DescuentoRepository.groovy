package mx.lux.pos.repository

import mx.lux.pos.model.Descuento
import mx.lux.pos.repository.custom.DescuentoRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

public interface DescuentoRepository extends JpaRepository<Descuento, Integer>, QueryDslPredicateExecutor<Descuento>, DescuentoRepositoryCustom {

  List<Descuento> findByClave( String pClave )
  
  List<Descuento> findByIdFactura( String pIdFactura )
}
