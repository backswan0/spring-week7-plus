package org.example.expert.domain.comment.repository;

import java.util.List;
import org.example.expert.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 주어진 todoId에 해당하는 Comment 목록 및 해당 Comment와 연관된 'user' 엔티티를 함께 조회
    @Query(
        "SELECT c FROM Comment c "
            + "JOIN FETCH c.user "
            + "WHERE c.todo.id = :todoId"
    )
    List<Comment> findByTodoIdWithUser(
        @Param("todoId") Long todoId
    );
}