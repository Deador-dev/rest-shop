package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.entity.Smartphone;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.CategoryService;
import com.deador.restshop.service.SmartphoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SmartphoneServiceImpl implements SmartphoneService {
    private static final String SMARTPHONE_NOT_FOUND_BY_ID = "Smartphone not found by id: %s";

    private static final String SMARTPHONE_ALREADY_EXIST_WITH_NAME = "Smartphone already exist with name: %s";
    private final SmartphoneRepository smartphoneRepository;
    private final CategoryService categoryService;
    private final DTOConverter dtoConverter;

    @Autowired
    public SmartphoneServiceImpl(SmartphoneRepository smartphoneRepository,
                                 CategoryService categoryService,
                                 DTOConverter dtoConverter) {
        this.smartphoneRepository = smartphoneRepository;
        this.categoryService = categoryService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<SmartphoneResponse> getAllSmartphones() {
        List<SmartphoneResponse> smartphoneResponses = smartphoneRepository.findAll().stream()
                .map(smartphone -> (SmartphoneResponse) dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class))
                .collect(Collectors.toList());

        log.debug("**/getting list of smartphones = " + smartphoneResponses);
        return smartphoneResponses;
    }

    @Override
    public SmartphoneResponse getSmartphoneResponseById(Long id) {
        Smartphone smartphone = smartphoneRepository.findById(id)
                .orElseThrow(() -> new NotExistException(String.format(SMARTPHONE_NOT_FOUND_BY_ID, id)));

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        log.debug("**/getting smartphone by id = " + smartphone);

        return smartphoneResponse;
    }

    @Override
    public SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile) {
        if (smartphoneRepository.existsByName(smartphoneProfile.getName())) {
            throw new AlreadyExistException(String.format(SMARTPHONE_ALREADY_EXIST_WITH_NAME, smartphoneProfile.getName()));
        }

        Smartphone smartphone = smartphoneRepository.save(dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class));
        log.debug("**/adding new smartphone = " + smartphone);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        return smartphoneResponse;
    }
}
