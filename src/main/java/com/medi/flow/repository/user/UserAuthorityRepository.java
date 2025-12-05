package com.medi.flow.repository.user;

import com.medi.flow.entity.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

}
