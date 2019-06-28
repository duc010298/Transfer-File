package com.github.duc010298.transferfile.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_role")
public class AppRoleEntity {
    private long roleId;
    private String roleName;
    private Set<AppUserEntity> appUserEntities = new HashSet<>(0);

    @Id
    @Column(name = "role_id")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Basic
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "appRoleEntities")
    public Set<AppUserEntity> getAppUserEntities() {
        return this.appUserEntities;
    }

    public void setAppUserEntities(Set<AppUserEntity> appUserEntities) {
        this.appUserEntities = appUserEntities;
    }
}
