package com.medi.flow.repository;

import com.medi.flow.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByActiveTrue(Pageable pageable);

    Optional<User> findOneActiveTrueById(Long id);

    Optional<User> findByEmailAndActiveTrue(String email);
}
