package com.example.UserManagementSystem.repository;

import com.example.UserManagementSystem.entities.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant,Long> {
    Variant findByUniqueId(Long aLong);


    void deleteByUniqueId(Long uniqueId);

    List<Variant> findByProductId(Long uniqueProductId);
}
