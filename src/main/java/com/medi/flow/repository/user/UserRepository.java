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

    @Query(nativeQuery = true,
            value = "select users.* " +//
                    "from usr_users users " +//
                    "where users.active = true " +//
                    "  and users.id = :userId " +//
                    "  and (exists(select 1 " +//
                    "              from usr_users_authorities sub_authorities " +//
                    "                       join adm_modules sub_modules " +//
                    "                            on sub_authorities.adm_module_id = sub_modules.id " +//
                    "                                and sub_modules.active = true " +//
                    "                       join adm_roles sub_roles " +//
                    "                            on sub_modules.adm_role_id = sub_roles.id " +//
                    "                                and sub_roles.active = true " +//
                    "              where sub_authorities.active = true " +//
                    "                and users.id = sub_authorities.usr_user_id " +//
                    "                and sub_roles.name = :authority)) ")
    Optional<User> findByIdAndAuthority(@Param("userId") Long userId, @Param("authority") String authority);
}
