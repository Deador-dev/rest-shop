package com.deador.restshop.repository;

import com.deador.restshop.model.SmartphoneImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmartphoneImageRepository extends JpaRepository<SmartphoneImage, Long> {
    List<SmartphoneImage> findAllBySmartphoneId(Long id);

    Optional<SmartphoneImage> findById(Long id);

    boolean existsById(Long id);
}