package com.petriuk.dao;

import com.petriuk.entity.Role;

import java.sql.SQLException;
import java.util.Optional;

public interface RoleDao {
    void create(Role role);
    void update(Role role);
    void remove(Role role);
    Optional<Role> findByName(String name);
}
