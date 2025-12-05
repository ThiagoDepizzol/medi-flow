package com.medi.flow.repository.user;

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

    @Query("select u " +//
            "from User u " +//
            "        left join fetch u.roles r " +//
            "        left join fetch r.module m " +//
            "        left join fetch m.role role " +//
            "where u.email = :email " +//
            "  and u.active = true " +//
            "  and r.active = true " +//
            "  and m.active = true ")
    Optional<User> findByEmailWithAuthorities(String email);
}
