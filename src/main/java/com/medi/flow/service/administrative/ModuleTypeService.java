package com.medi.flow.service.administrative;

import com.medi.flow.entity.administrative.Role;
import com.medi.flow.enumerated.ModuleType;
import com.medi.flow.enumerated.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleTypeService {

    private final Logger log = LoggerFactory.getLogger(ModuleTypeService.class);

    private final RoleService roleService;

    private final ModuleService moduleService;

    public ModuleTypeService(final RoleService roleService, final ModuleService moduleService) {
        this.roleService = roleService;
        this.moduleService = moduleService;
    }

    public void verifyIfExistNewModuleType() {

        log.info("Verifying if exist new module type");

        List.of(ModuleType.values())
                .forEach(type -> roleService.findOneByNameAndPrefix(type.getName(), type.getPrefix())
                        .ifPresentOrElse(moduleService::verifyIfExistModuleWithType,
                                () -> {

                                    final Role newRole = new Role();

                                    newRole.setActive(true);
                                    newRole.setName(type.getName());
                                    newRole.setPrefix(type.getPrefix());
                                    newRole.setStatus(Status.ACTIVE);

                                    roleService.created(newRole)
                                            .ifPresent(savedRole -> {
                                                // TODO: Implementar ModuleService
                                            });
                                }));


    }
}
