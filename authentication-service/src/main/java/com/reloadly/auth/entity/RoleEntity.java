package com.reloadly.auth.entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * The Role entity.
 *
 * @author Arun Patra
 */
@Entity
@Table(name = "role_table")
public class RoleEntity extends AbstractPersistable<Long> {

    @Column(name = "role_name")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
