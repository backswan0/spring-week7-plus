package org.example.expert.domain.todo.repository;

import static org.example.expert.common.entity.QTodo.todo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.todo.dto.request.TodoDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // todo private으로 두 메서드를 처리하고, 이 둘을 감싸서 한 번에 처리
    // todo 검색 조건과 무관하게 생성일 최신순으로 정렬하기
    public List<Todo> search(
        TodoDto dto,
        Pageable pageable
    ) {
        return jpaQueryFactory.selectFrom(todo)
            .leftJoin(todo.user).fetchJoin()
            .where(containsTitle(dto.getTitle()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    public Optional<Todo> findByIdWithUser(long todoId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(todo)
            .leftJoin(todo.user).fetchJoin()
            .where(todo.id.eq(todoId))
            .fetchOne());
    }

    private BooleanExpression containsTitle(String title) {
        if (title == null) {
            return null;
        }
        return todo.title.contains(title);
    }

    public long countByTitle(String search) {
        return Optional.ofNullable(
                jpaQueryFactory.select(Wildcard.count)
                    .from(todo)
                    .where(todo.title.contains(search))
                    .fetchOne()
            )
            .orElse(0L);
    }
}