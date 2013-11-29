package mx.lux.pos.repository

import mx.lux.pos.model.FormaPago
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface FormaPagoRepository extends JpaRepository<FormaPago, String>, QueryDslPredicateExecutor<FormaPago> {
}
