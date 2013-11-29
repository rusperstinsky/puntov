package mx.lux.pos.repository

import mx.lux.pos.model.NotaFactura
import mx.lux.pos.repository.custom.NotaFacturaRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface NotaFacturaRepository extends JpaRepository<NotaFactura, String>, QueryDslPredicateExecutor<NotaFactura>, NotaFacturaRepositoryCustom {
}
