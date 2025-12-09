package com.medi.flow.mapper.user;

import com.medi.flow.dto.user.UserDTO;
import com.medi.flow.entity.user.User;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);


    public UserDTO fromDto(@NotNull final User user) {

        log.info("fromUser() -> {}", user);

        final UserDTO newDto = new UserDTO();

        newDto.setId(user.getId());
        newDto.setActive(user.getActive());
        newDto.setFirstName(user.getFirstName());
        newDto.setLastName(user.getLastName());
        newDto.setEmail(user.getEmail());
        newDto.setActivated(user.getActivated());

        return newDto;
    }
}
