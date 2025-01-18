package org.example.expert.domain.todo.repository;

import static org.example.expert.common.entity.QTodo.todo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.todo.dto.request.TodoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Todo> search(
        TodoDto dto,
        Pageable pageable
    ) {
        List<Todo> todoList = new ArrayList<>();

        todoList = findTodoList(dto, pageable);

        long totalElements = countByTitle(dto.getTitle());

        return new PageImpl<>(
            todoList,
            pageable,
            totalElements
        );
    }

    public Optional<Todo> findByIdWithUser(long todoId) {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne()
        );
    }

    private List<Todo> findTodoList(
        TodoDto dto,
        Pageable pageable
    ) {
        List<Todo> todoList = new ArrayList<>();

        todoList = jpaQueryFactory.selectFrom(todo)
            .leftJoin(todo.user).fetchJoin()
            .where(containsTitle(dto.getTitle()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(todo.createdAt.desc())
            .fetch();

        return todoList;
    }

    private BooleanExpression containsTitle(String title) {
        if (title == null) {
            return null;
        }
        return todo.title.contains(title);
    }

    private long countByTitle(String search) {
        return Optional.ofNullable(
                jpaQueryFactory.select(Wildcard.count)
                    .from(todo)
                    .where(todo.title.contains(search))
                    .fetchOne()
            )
            .orElse(0L);
    }
}