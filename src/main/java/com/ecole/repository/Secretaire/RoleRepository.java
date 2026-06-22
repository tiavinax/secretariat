package com.ecole.repository.Secretaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecole.entity.Secretaire.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNom(String nom);
}