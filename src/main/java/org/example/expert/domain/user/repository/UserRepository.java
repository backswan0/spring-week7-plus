package org.example.expert.domain.user.repository;

import java.util.Optional;
import org.example.expert.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 주어진 이메일로 사용자 조회
    Optional<User> findByEmail(String email);

    // 주어진 이메일로 사용자가 존재하는지 확인
    boolean existsByEmail(String email);
}
