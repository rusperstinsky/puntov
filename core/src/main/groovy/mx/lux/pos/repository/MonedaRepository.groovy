package mx.lux.pos.repository

import org.springframework.data.jpa.repository.JpaRepository

import mx.lux.pos.model.Moneda
import org.springframework.data.querydsl.QueryDslPredicateExecutor

public interface MonedaRepository extends JpaRepository<Moneda, String>, QueryDslPredicateExecutor<Moneda> {

}