package com.medi.flow.mapper.administrative;

import com.medi.flow.dto.administrative.ModuleDTO;
import com.medi.flow.entity.administrative.Module;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapper {

    private static final Logger log = LoggerFactory.getLogger(ModuleMapper.class);


    public ModuleDTO fromDto(@NotNull final Module module) {

        log.info("fromDto() -> {}", module);

        final ModuleDTO newDto = new ModuleDTO();

        newDto.setId(module.getId());
        newDto.setName(module.getName());
        newDto.setStatus(module.getStatus());
        newDto.setDescription(module.getDescription());
        newDto.setView(module.getView());
        newDto.setCreated(module.getCreated());
        newDto.setEdit(module.getEdit());
        newDto.setDelete(module.getDelete());

        return newDto;
    }

}
