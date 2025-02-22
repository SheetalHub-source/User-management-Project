package com.example.UserManagementSystem.repository;

import com.example.UserManagementSystem.entities.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users,Long>, JpaSpecificationExecutor<Users> {
    Users findByUniqueId(Long aLong);

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

}
