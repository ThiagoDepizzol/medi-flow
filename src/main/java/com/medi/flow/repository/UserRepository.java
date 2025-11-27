package com.medi.flow.repository;

import com.medi.flow.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByActiveTrue(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select users.* " +//
                    "from usr_users users " +//
                    "where users.active = true " +//
                    "  and users.id = :userId ")
    Optional<User> findOneActiveTrueById(@Param("userId") Long userId);

    Optional<User> findByEmailAndActiveTrue(String email);
}
