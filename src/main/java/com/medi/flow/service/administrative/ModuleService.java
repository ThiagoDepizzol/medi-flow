package com.medi.flow.service.administrative;

import com.medi.flow.entity.administrative.Module;
import com.medi.flow.entity.administrative.Role;
import com.medi.flow.enumerated.ModuleType;
import com.medi.flow.enumerated.Status;
import com.medi.flow.repository.administrative.ModuleRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleService.class);

    public final ModuleRepository moduleRepository;

    public ModuleService(final ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Optional<Module> created(@NotNull final Module module) {

        logger.info("created() -> {}", module);

        return Optional.of(moduleRepository.save(module));

    }

    public Optional<Module> update(@NotNull final Long id, @NotNull final Module module) {

        logger.info("update() -> {}, {}", id, module);

        return Optional.of(moduleRepository.save(module));

    }

    @Transactional(readOnly = true)
    public Page<Module> findAll(@NotNull final Pageable pageable) {

        logger.info("findAll() -> {}", pageable);

        return moduleRepository.findAllByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Module> findById(@NotNull final Long id) {

        logger.info("findById() -> {}", id);

        return moduleRepository.findOneActiveTrueById(id);
    }


    public void delete(@NotNull final Module module) {

        logger.info("delete() -> {}", module);

        module.setActive(false);

        moduleRepository.save(module);

    }

    public void verifyIfExistByRole(@NotNull final Role role, @NotNull final ModuleType type) {

        logger.info("verifyIfExistByRole() -> {}, {}", role, type);

        if (moduleRepository.findOneByRole(role.getId()).isEmpty()) {

            final Module newModule = new Module();

            newModule.setActive(true);
            newModule.setRole(role);
            newModule.setStatus(Status.ACTIVE);
            newModule.setName(type.getName());
            newModule.setView(true);
            newModule.setCreated(true);
            newModule.setEdit(true);
            newModule.setDelete(true);

            moduleRepository.save(newModule);

        }

    }

}
