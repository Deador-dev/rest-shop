package com.deador.restshop.repository;

import com.deador.restshop.model.Smartphone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {
    List<Smartphone> findAll();

    List<Smartphone> findAllByCategoryId(Long id);

    Page<Smartphone> findAllByCategoryId(Long id, Pageable pageable);

    Optional<Smartphone> findById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);
}