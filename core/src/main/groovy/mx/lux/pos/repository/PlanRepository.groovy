package mx.lux.pos.repository

import mx.lux.pos.model.Plan
import mx.lux.pos.repository.custom.PlanRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface PlanRepository extends JpaRepository<Plan, String>, QueryDslPredicateExecutor<Plan>, PlanRepositoryCustom {
}
