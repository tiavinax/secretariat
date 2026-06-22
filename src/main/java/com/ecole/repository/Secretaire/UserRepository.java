package com.ecole.repository.Secretaire;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecole.entity.Secretaire.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    public User findByEmail(String email);
} 