package org.example.expert.domain.manager.repository;

import java.util.List;
import org.example.expert.common.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    @Query(
        "SELECT m FROM Manager m "
            + "JOIN FETCH m.user "
            + "WHERE m.todo.id = :todoId"
    )
    List<Manager> findByTodoIdWithUser(
        @Param("todoId") Long todoId
    );
}
