package com.deador.restshop.repository;

import com.deador.restshop.entity.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {
    List<Smartphone> findAll();

    List<Smartphone> findAllByCategoryId(Long id);

    Optional<Smartphone> findById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}