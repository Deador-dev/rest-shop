package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.dto.smartphone.UpdateSmartphoneIsDiscountActive;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.BadRequestException;
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
    private static final String SMARTPHONE_IS_DISCOUNT_ACTIVE_UPDATING_ERROR = "The status of isDiscountActive for the smartphone with id %s cannot be updated because the discountPercent is not equal to 0";
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

        log.debug("get list of smartphone responses '{}' ", smartphoneResponses);
        return smartphoneResponses;
    }

    @Override
    public List<Smartphone> getAllSmartphonesByCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        }
        List<Smartphone> smartphones = smartphoneRepository.findAllByCategoryId(id);

        log.debug("get list of smartphones '{}' by category id '{}'", smartphones, id);
        return smartphones;
    }

    public Smartphone getSmartphoneById(Long id) {
        log.debug("get smartphone by id '{}'", id);
        return smartphoneRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("smartphone not found by id '{}'", id);
                    return new NotExistException(String.format(SMARTPHONE_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public SmartphoneResponse getSmartphoneResponseById(Long id) {
        Smartphone smartphone = getSmartphoneById(id);
        smartphone.setCountOfViews(smartphone.getCountOfViews() + 1L);
        log.debug("the count of views for smartphone with id '{}' was updated to '{}'", id, smartphone.getCountOfViews());
        smartphoneRepository.save(smartphone);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        log.debug("get smartphone responses by id '{}'", id);
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
        log.debug("smartphone was created successfully '{}'", smartphone);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        return smartphoneResponse;
    }

    @Override
    public SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile) {
        Smartphone smartphoneFromDB = getSmartphoneById(id);
        Smartphone smartphoneFromDTO = dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class);

        smartphoneFromDTO.setId(id);
        smartphoneFromDTO.setCategory(categoryService.getCategoryById(smartphoneProfile.getCategoryId()));
        smartphoneFromDTO.setIsDiscountActive(smartphoneFromDB.getIsDiscountActive());
        smartphoneFromDTO.setDiscountPercent(smartphoneFromDB.getDiscountPercent());
        smartphoneFromDTO.setDiscountedPrice(smartphoneFromDB.getDiscountedPrice());

//        smartphoneFromDTO = smartphoneRepository.save(smartphoneFromDTO);
//        smartphoneRepository.flush();

        // FIXME: 20.04.2023 need to call updateIsDiscountActiveById if price was changed
//        if (!smartphoneFromDB.getPrice().equals(smartphoneProfile.getPrice())) {
//            UpdateSmartphoneIsDiscountActive updateSmartphoneIsDiscountActive = UpdateSmartphoneIsDiscountActive
//                    .builder()
//                    .isDiscountActive(smartphoneFromDTO.getIsDiscountActive())
//                    .discountPercent(smartphoneFromDTO.getDiscountPercent())
//                    .build();
//
//            SmartphoneResponse smartphoneWithUpdatedIsDiscountActive = updateIsDiscountActiveById(id, updateSmartphoneIsDiscountActive);
//            smartphoneFromDTO.setIsDiscountActive(smartphoneWithUpdatedIsDiscountActive.getIsDiscountActive());
//            smartphoneFromDTO.setDiscountPercent(smartphoneWithUpdatedIsDiscountActive.getDiscountPercent());
//            smartphoneFromDTO.setDiscountedPrice(smartphoneWithUpdatedIsDiscountActive.getDiscountedPrice());
//
//            smartphoneFromDTO = smartphoneRepository.save(smartphoneFromDTO);
//        }

        log.debug("smartphone was updated successfully by id '{}'", id);
        return dtoConverter.convertToDTO(smartphoneRepository.save(smartphoneFromDTO), SmartphoneResponse.class);
    }

    @Override
    public SmartphoneResponse updateIsDiscountActiveById(Long id, UpdateSmartphoneIsDiscountActive updateSmartphoneIsDiscountActive) {
        Smartphone smartphone = getSmartphoneById(id);

        if (!updateSmartphoneIsDiscountActive.getIsDiscountActive() && updateSmartphoneIsDiscountActive.getDiscountPercent() != 0) {
            throw new BadRequestException(String.format(SMARTPHONE_IS_DISCOUNT_ACTIVE_UPDATING_ERROR, id));
        }

        smartphone.setIsDiscountActive(updateSmartphoneIsDiscountActive.getIsDiscountActive());
        smartphone.setDiscountPercent(updateSmartphoneIsDiscountActive.getDiscountPercent());

        if (updateSmartphoneIsDiscountActive.getIsDiscountActive()) {
            smartphone.setDiscountedPrice(smartphone.getPrice() * (1 - updateSmartphoneIsDiscountActive.getDiscountPercent() / 100.0));
        } else {
            smartphone.setDiscountedPrice(0.0);
        }

        log.debug("the isDiscountActive of the smartphone with id '{}' was successfully updated to '{}' with discount percent '{}'",
                id, updateSmartphoneIsDiscountActive.getIsDiscountActive(), updateSmartphoneIsDiscountActive.getDiscountPercent());
        return dtoConverter.convertToDTO(smartphoneRepository.save(smartphone), SmartphoneResponse.class);
    }

    @Override
    public SmartphoneResponse deleteSmartphoneById(Long id) {
        Smartphone smartphone = getSmartphoneById(id);

        try {
            smartphoneRepository.deleteById(id);
            smartphoneRepository.flush();
            // TODO: 15.04.2023 need to delete cartItem, orderItem, and images in future
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(SMARTPHONE_DELETING_ERROR);
        }

        log.debug("smartphone was successfully deleted by id '{}'", id);
        return dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
    }
}
