package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.entity.Category;
import com.deador.restshop.entity.Smartphone;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.DatabaseRepositoryException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.CategoryService;
import com.deador.restshop.service.SmartphoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SmartphoneServiceImpl implements SmartphoneService {
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String SMARTPHONE_NOT_FOUND_BY_ID = "Smartphone not found by id: %s";
    private static final String SMARTPHONE_ALREADY_EXIST_WITH_NAME = "Smartphone already exist with name: %s";
    private static final String SMARTPHONE_DELETING_ERROR = "Can't delete smartphone cause of relationships";
    private final CategoryRepository categoryRepository;
    private final SmartphoneRepository smartphoneRepository;
    private final CategoryService categoryService;
    private final DTOConverter dtoConverter;

    @Autowired
    public SmartphoneServiceImpl(CategoryRepository categoryRepository,
                                 SmartphoneRepository smartphoneRepository,
                                 CategoryService categoryService,
                                 DTOConverter dtoConverter) {
        this.categoryRepository = categoryRepository;
        this.smartphoneRepository = smartphoneRepository;
        this.categoryService = categoryService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<SmartphoneResponse> getAllSmartphoneResponses() {
        List<SmartphoneResponse> smartphoneResponses = smartphoneRepository.findAll().stream()
                .map(smartphone -> (SmartphoneResponse) dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class))
                .collect(Collectors.toList());

        log.debug("getting list of smartphones = " + smartphoneResponses);
        return smartphoneResponses;
    }

    @Override
    public List<Smartphone> getAllSmartphonesByCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        }
        List<Smartphone> smartphones = smartphoneRepository.findAllByCategoryId(id);

        log.debug("getting list of smartphones = " + smartphones);
        return smartphones;
    }

    public Smartphone getSmartphoneById(Long id) {
        return smartphoneRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("smartphone not found by id {}", id);
                    return new NotExistException(String.format(SMARTPHONE_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public SmartphoneResponse getSmartphoneResponseById(Long id) {
        Smartphone smartphone = getSmartphoneById(id);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        log.debug("getting smartphone by id = " + smartphone);

        return smartphoneResponse;
    }

    @Override
    public SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile) {
        if (smartphoneRepository.existsByName(smartphoneProfile.getName())) {
            throw new AlreadyExistException(String.format(SMARTPHONE_ALREADY_EXIST_WITH_NAME, smartphoneProfile.getName()));
        } else if (!categoryRepository.existsById(smartphoneProfile.getCategoryId())) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, smartphoneProfile.getCategoryId()));
        }

        Smartphone smartphone = smartphoneRepository.save(dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class));
        log.debug("adding new smartphone = " + smartphone);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        return smartphoneResponse;
    }

    @Override
    public SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile) {
        if (!smartphoneRepository.existsById(id)) {
            throw new NotExistException(String.format(SMARTPHONE_NOT_FOUND_BY_ID, id));
        }

        Category category = null;
        if (smartphoneProfile.getCategoryId() != null) {
            category = categoryService.getCategoryById(smartphoneProfile.getCategoryId());
        }

        Smartphone updatedSmartphone = dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class);
        updatedSmartphone.setId(id);
        updatedSmartphone.setCategory(category);

        log.debug("updating smartphone by id {}", updatedSmartphone);
        return dtoConverter.convertToDTO(smartphoneRepository.save(updatedSmartphone), SmartphoneResponse.class);
    }

    @Override
    public SmartphoneResponse deleteSmartphoneById(Long id) {
        Smartphone smartphone = getSmartphoneById(id);

        try {
            smartphoneRepository.deleteById(id);
            // TODO: 15.04.2023 need to delete cartItem, orderItem, and images in future
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(SMARTPHONE_DELETING_ERROR);
        }

        log.debug("smartphone was successfully deleted = {}", smartphone);

        return dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
    }
}
