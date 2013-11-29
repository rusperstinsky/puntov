package mx.lux.pos.repository

import mx.lux.pos.model.TrabajoTrack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QueryDslPredicateExecutor

interface TrabajoTrackRepository extends JpaRepository<TrabajoTrack, String>, QueryDslPredicateExecutor<TrabajoTrack> {
}
