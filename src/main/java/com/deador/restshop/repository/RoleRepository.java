package com.deador.restshop.repository;

import com.deador.restshop.entity.Role;
import com.deador.restshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}