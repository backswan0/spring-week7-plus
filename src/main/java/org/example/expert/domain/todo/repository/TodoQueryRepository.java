package org.example.expert.domain.todo.repository;

import static org.example.expert.common.entity.QTodo.todo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.Todo;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Todo> findByIdWithUser(long todoId) {

        return Optional.ofNullable(jpaQueryFactory.selectFrom(todo)
            .leftJoin(todo.user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchOne());
    }
}