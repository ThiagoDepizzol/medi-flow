package com.medi.flow.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medi.flow.entity.administrative.Module;
import com.medi.flow.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "usr_users_authorities")
public class UserAuthority extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "usr_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "roles", allowSetters = true)
    private User user;

    @NotNull
    @JoinColumn(name = "adm_module_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Module module;

    public UserAuthority() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserAuthority that = (UserAuthority) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
