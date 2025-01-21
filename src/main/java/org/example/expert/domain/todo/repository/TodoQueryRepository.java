package org.example.expert.domain.todo.repository;

import static com.querydsl.jpa.JPAExpressions.select;
import static org.example.expert.common.entity.QComment.comment;
import static org.example.expert.common.entity.QManager.manager;
import static org.example.expert.common.entity.QTodo.todo;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.todo.dto.request.TodoSearchRequestDto;
import org.example.expert.domain.todo.dto.response.TodoSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 주어진 조건에 맞는 일정 리스트를 페이징 처리하여 반환
    public Page<TodoSearchResponseDto> search(
        TodoSearchRequestDto dto,
        Pageable pageable
    ) {
        List<TodoSearchResponseDto> todoList = new ArrayList<>();

        todoList = findTodoList(dto, pageable);

        long totalElements = countTotalElements(dto);

        return new PageImpl<>(
            todoList,
            pageable,
            totalElements
        );
    }

    // 일정 ID로 일정과 관련된 User 정보를 함께 조회
    public Optional<Todo> findByIdWithUser(long todoId) {
        return Optional.ofNullable(
            jpaQueryFactory.selectFrom(todo)
                .leftJoin(todo.user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne()
        );
    }

    // 검색 조건에 맞는 일정 목록을 조회
    private List<TodoSearchResponseDto> findTodoList(
        TodoSearchRequestDto dto,
        Pageable pageable
    ) {
        List<TodoSearchResponseDto> todoList = new ArrayList<>();

        todoList = jpaQueryFactory.select(
                Projections.constructor(
                    TodoSearchResponseDto.class,
                    todo.title,
                    select(manager.id.count())
                        .from(manager)
                        .where(manager.todo.id.eq(todo.id)),
                    select(comment.count())
                        .from(comment)
                        .where(comment.todo.id.eq(todo.id))
                )
            )
            .from(todo)
            .where(
                containsTitle(dto.getTitle()),
                goeStartsAt(dto.getStartsAt()),
                loeEndsAt(dto.getEndsAt()),
                containsNickname(dto.getNickname())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(todo.createdAt.desc())
            .fetch();

        return todoList;
    }

    // 제목이 주어진 값으로 포함되는 일정을 찾는 조건 생성
    private BooleanExpression containsTitle(String title) {
        if (title == null) {
            return null;
        }
        return todo.title.contains(title);
    }

    // 시작 시간이 주어진 값 이후인 일정을 찾는 조건 생성
    private BooleanExpression goeStartsAt(LocalDateTime startsAt) {
        if (startsAt == null) {
            return null;
        }
        return todo.createdAt.goe(startsAt);
    }

    // 종료 시간이 주어진 값 이전인 일정을 찾는 조건 생성
    private BooleanExpression loeEndsAt(LocalDateTime endsAt) {
        if (endsAt == null) {
            return null;
        }
        return todo.createdAt.loe(endsAt);
    }

    // 담당자의 닉네임이 주어진 값으로 포함되는 일정을 찾는 조건 생성
    private BooleanExpression containsNickname(String nickname) {
        if (nickname == null) {
            return null;
        }
        return todo.managers.any().user.nickname.contains(nickname);
    }

    // 검색 조건에 맞는 일정의 총 개수 반환
    private long countTotalElements(TodoSearchRequestDto dto) {
        return Optional.ofNullable(
                jpaQueryFactory.select(todo.count())
                    .from(todo)
                    .where(
                        containsTitle(dto.getTitle()),
                        goeStartsAt(dto.getStartsAt()),
                        loeEndsAt(dto.getEndsAt()),
                        containsNickname(dto.getNickname())
                    )
                    .fetchOne()
            )
            .orElse(0L);
    }
}