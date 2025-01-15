package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.expert.common.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t FROM Todo t "
        + "LEFT JOIN FETCH t.user u "
        + "ORDER BY t.updatedAt DESC")
    Page<Todo> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t "
        + "WHERE (:weather IS NULL OR t.weather = :weather)")
    Page<Todo> findAllByWeather(
        @Param("weather") String weather,
        Pageable pageable
    );

    @Query("SELECT t FROM Todo t "
        + "WHERE (:startsAt IS NULL OR t.updatedAt >= :startsAt) "
        + "AND (:endsAt IS NULL OR t.updatedAt <= :endsAt) "
        + "ORDER BY t.updatedAt DESC")
    Page<Todo> findAllByUpdatedAtBetween(
        @Param("startsAt") LocalDateTime startsAt,
        @Param("endsAt") LocalDateTime endsAt,
        Pageable pageable
    );

    @Query("SELECT t FROM Todo t " +
        "LEFT JOIN t.user " +
        "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}