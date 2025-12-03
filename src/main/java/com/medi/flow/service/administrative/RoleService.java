package com.medi.flow.service.administrative;

import com.medi.flow.entity.administrative.Role;
import com.medi.flow.enumerated.ModuleType;
import com.medi.flow.enumerated.Status;
import com.medi.flow.repository.administrative.RoleRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public final RoleRepository roleRepository;

    public final ModuleService moduleService;

    public RoleService(final RoleRepository roleRepository, final ModuleService moduleService) {
        this.roleRepository = roleRepository;
        this.moduleService = moduleService;
    }

    public Optional<Role> created(@NotNull final Role role) {

        logger.info("created() -> {}", role);

        return Optional.of(roleRepository.save(role));

    }

    public Optional<Role> update(@NotNull final Long id, @NotNull final Role role) {

        logger.info("update() -> {}, {}", id, role);

        return Optional.of(roleRepository.save(role));

    }

    @Transactional(readOnly = true)
    public Page<Role> findAll(@NotNull final Pageable pageable) {

        logger.info("findAll() -> {}", pageable);

        return roleRepository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Role> findById(@NotNull final Long id) {

        logger.info("findById() -> {}", id);

        return roleRepository.findOneActiveTrueById(id);
    }


    public void delete(@NotNull final Role role) {

        logger.info("delete() -> {}", role);

        role.setActive(false);

        roleRepository.save(role);

    }

    public Optional<Role> findOneByNameAndPrefix(@NotNull final String name, @NotNull final String prefix) {

        logger.info("findOneByNameAndPrefix() -> {}, {}", name, prefix);

        return roleRepository.findOneByNameAndPrefix(name, prefix);

    }

    public void newByModuleType(@NotNull final ModuleType type) {

        logger.info("newByModuleType() -> {}", type);

        final Role newRole = new Role();

        newRole.setActive(true);
        newRole.setStatus(Status.ACTIVE);
        newRole.setName(type.getName());
        newRole.setPrefix(type.getPrefix());

        created(newRole)
                .ifPresent(savedRole -> moduleService.verifyIfExistByRole(savedRole, type));
    }
}
