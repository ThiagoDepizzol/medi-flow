package com.medi.flow.entity.administrative;

import com.medi.flow.entity.base.BaseEntity;
import com.medi.flow.enumerated.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "adm_modules")
public class Module extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "adm_role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;

    @NotNull
    @Column(unique = true)
    private Boolean view;

    @NotNull
    @Column(unique = true)
    private Boolean created;

    @NotNull
    @Column(unique = true)
    private Boolean edit;

    @NotNull
    @Column(unique = true)
    private Boolean delete;

    public Module() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getCreated() {
        return created;
    }

    public void setCreated(Boolean created) {
        this.created = created;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Module role = (Module) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
