package com.deador.restshop.repository;

import com.deador.restshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();

    Optional<Category> findById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}