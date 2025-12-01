package com.medi.flow.config;

import com.medi.flow.service.administrative.ModuleTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ApplicationStart implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(ApplicationStart.class);

    private final ModuleTypeService moduleTypeService;

    public ApplicationStart(final ModuleTypeService moduleTypeService) {
        this.moduleTypeService = moduleTypeService;
    }

    @Override
    public void run(String... args) {
        try {

            log.info("Starting configuration");

            moduleTypeService.verifyIfExistNewModuleType();


        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error starting application");
        }
    }

}
