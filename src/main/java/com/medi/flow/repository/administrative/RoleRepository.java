package com.medi.flow.repository.administrative;

import com.medi.flow.entity.administrative.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Page<Role> findAllByActiveTrue(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select roles.* " +//
                    "from adm_roles roles " +//
                    "where roles.active = true " +//
                    "  and roles.id = :roleId ")
    Optional<Role> findOneActiveTrueById(@Param("roleId") Long roleId);

    @Query(nativeQuery = true,
            value = "select roles.* " +//
                    "from adm_roles roles " +//
                    "where roles.active = true " +//
                    "  and roles.name = :name " +//
                    "  and roles.prefix = :prefix " +//
                    "limit 1 ")
    Optional<Role> findOneByNameAndPrefix(@Param("name") String name, @Param("prefix") String prefix);


}
