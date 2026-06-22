package com.ecole.repository.Secretaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ecole.entity.Secretaire.UserRole;
import com.ecole.entity.Secretaire.UserRoleId;
import com.ecole.entity.Secretaire.User;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser(User user);
}