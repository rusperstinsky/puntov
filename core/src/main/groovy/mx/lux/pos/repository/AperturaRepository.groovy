package mx.lux.pos.repository

import mx.lux.pos.model.Apertura
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

public interface AperturaRepository extends JpaRepository<Apertura, Date>, QueryDslPredicateExecutor<Apertura>{

}