package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import org.example.expert.common.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @EntityGraph(attributePaths = "user")
    @Query(
        "SELECT t FROM Todo t "
            + "ORDER BY t.updatedAt DESC"
    )
    Page<Todo> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    @Query(
        "SELECT t FROM Todo t "
            + "WHERE (:weather IS NULL OR t.weather = :weather)"
    )
    Page<Todo> findAllByWeather(
        @Param("weather") String weather,
        Pageable pageable
    );

    @EntityGraph(attributePaths = "user")
    @Query(
        "SELECT t FROM Todo t "
            + "WHERE (:startsAt IS NULL OR t.updatedAt >= :startsAt) "
            + "AND (:endsAt IS NULL OR t.updatedAt <= :endsAt) "
            + "ORDER BY t.updatedAt DESC"
    )
    Page<Todo> findAllByUpdatedAtBetween(
        @Param("startsAt") LocalDateTime startsAt,
        @Param("endsAt") LocalDateTime endsAt,
        Pageable pageable
    );
}