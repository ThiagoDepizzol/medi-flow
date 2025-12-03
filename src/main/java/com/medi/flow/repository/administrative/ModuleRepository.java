package com.medi.flow.repository.administrative;

import com.medi.flow.entity.administrative.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    Page<Module> findAllByActiveTrue(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select modules.* " +//
                    "from adm_modules modules " +//
                    "where roles.active = true " +//
                    "  and modules.id = :moduleId ")
    Optional<Module> findOneActiveTrueById(@Param("moduleId") Long moduleId);

    @Query(nativeQuery = true, //
            value = "select modules.* " +//
                    "from adm_modules modules " +//
                    "         join adm_roles roles on modules.adm_role_id = roles.id " +//
                    "where modules.active = true " +//
                    "  and roles.active = true " +//
                    "  and roles.id = :roleId " +//
                    "  and roles.status = 'ACTIVE' ")
    Optional<Module> findOneByRole(@Param("roleId") Long roleId);

}
