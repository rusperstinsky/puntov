package mx.lux.pos.repository

import mx.lux.pos.model.MensajeAlt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface MensajeAltRepository extends JpaRepository<MensajeAlt, Integer>, QueryDslPredicateExecutor<MensajeAlt> {

}
