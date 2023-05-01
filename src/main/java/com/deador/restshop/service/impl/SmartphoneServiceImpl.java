package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.dto.smartphone.UpdateSmartphoneIsDiscountActive;
import com.deador.restshop.exception.*;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.CategoryService;
import com.deador.restshop.service.SmartphoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SmartphoneServiceImpl implements SmartphoneService {
    private final static String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/smartphoneImages/";
    private static final String DIRECTORY_CREATION_EXCEPTION = "Directory creation error with path: ";
    private static final String CATEGORY_NOT_FOUND_BY_ID = "Category not found by id: %s";
    private static final String SMARTPHONE_NOT_FOUND_BY_ID = "Smartphone not found by id: %s";
    private static final String SMARTPHONE_ALREADY_EXIST_WITH_NAME = "Smartphone already exist with name: %s";
    private static final String SMARTPHONE_DELETING_ERROR = "Can't delete smartphone cause of relationships";
    private static final String SMARTPHONE_IS_DISCOUNT_ACTIVE_UPDATING_ERROR = "The status of isDiscountActive for the smartphone with id %s cannot be updated because the discountPercent is not equal to 0";
    private static final String SMARTPHONES_NOT_FOUND_FOR_SPECIFIED_CATEGORY_AND_PAGE = "Smartphones not found for specified category %s and page %s";
    private static final String SORT_FIELD_NOT_FOUND = "Sort field not found";
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
    public List<SmartphoneResponse> getListOfSmartphoneResponses() {
        List<SmartphoneResponse> smartphoneResponses = smartphoneRepository.findAll().stream()
                .map(smartphone -> (SmartphoneResponse) dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of smartphone responses '{}' ", smartphoneResponses);
        return smartphoneResponses;
    }

    @Override
    public List<Smartphone> getListOfSmartphonesByCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, id));
        }
        List<Smartphone> smartphones = smartphoneRepository.findAllByCategoryId(id);

        log.debug("get list of smartphones '{}' by category id '{}'", smartphones, id);
        return smartphones;
    }

    @Override
    public Page<SmartphoneResponse> getListOfSmartphoneResponsesByCategoryId(Long id, Pageable pageable) {
        Page<Smartphone> smartphonePage = smartphoneRepository.findAllByCategoryId(id, pageable);

        if (pageable.getPageNumber() >= smartphonePage.getTotalPages()) {
            throw new NotExistException(String.format(SMARTPHONES_NOT_FOUND_FOR_SPECIFIED_CATEGORY_AND_PAGE,
                    categoryService.getCategoryById(id).getName(),
                    pageable.getPageNumber()));
        }

        log.debug("get list of smartphone responses by category id '{}' page '{}'", id, pageable.getPageNumber());
        return convertToSmartphoneResponsePage(smartphonePage);
    }

    @Override
    public Page<SmartphoneResponse> getListOfSmartphoneResponsesWithSorting(Pageable pageable) {
        String sortBy = pageable.getSort().stream()
                .findFirst()
                .orElseThrow(() -> {
                    log.error("sort field not found");
                    return new NotExistException(SORT_FIELD_NOT_FOUND);
                })
                .getProperty();
        log.debug("sort by '{}'", sortBy);

        Page<Smartphone> smartphonePage = switch (sortBy) {
            case "priceLowToHigh" -> smartphoneRepository.findAll(
                    getPageable(pageable, Sort.Direction.ASC, "price")
            );
            case "priceHighToLow" -> smartphoneRepository.findAll(
                    getPageable(pageable, Sort.Direction.DESC, "price")
            );
            case "avgCustomerReview" -> smartphoneRepository.findAll(
                    getPageable(pageable, Sort.Direction.DESC, "countOfViews")
            );
            default -> {
                log.error("sort field not found");
                throw new NotExistException(SORT_FIELD_NOT_FOUND);
            }
        };

        log.debug("get list of sorted smartphones '{}'", smartphonePage.getContent());
        return convertToSmartphoneResponsePage(smartphonePage);
    }

    private PageImpl<SmartphoneResponse> convertToSmartphoneResponsePage(Page<Smartphone> smartphonePage) {
        return new PageImpl<>(
                smartphonePage.stream()
                        .map(smartphone -> (SmartphoneResponse) dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class))
                        .collect(Collectors.toList()), smartphonePage.getPageable(), smartphonePage.getTotalElements()
        );
    }

    private static Pageable getPageable(Pageable pageable, Sort.Direction sortDirection, String sortField) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortDirection.name().equals("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
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
    public SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile, MultipartFile file) {
        if (smartphoneRepository.existsByName(smartphoneProfile.getName())) {
            throw new AlreadyExistException(String.format(SMARTPHONE_ALREADY_EXIST_WITH_NAME, smartphoneProfile.getName()));
        } else if (!categoryRepository.existsById(smartphoneProfile.getCategoryId())) {
            throw new NotExistException(String.format(CATEGORY_NOT_FOUND_BY_ID, smartphoneProfile.getCategoryId()));
        }

        Smartphone smartphone = dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class);

        addImageToSmartphone(smartphone, file);

        SmartphoneResponse smartphoneResponse = dtoConverter.convertToDTO(smartphoneRepository.save(smartphone), SmartphoneResponse.class);
        smartphoneResponse.setCategory(categoryService.getCategoryResponseById(smartphoneResponse.getCategory().getId()));

        log.debug("smartphone was created successfully '{}'", smartphone);
        return smartphoneResponse;
    }

    @Override
    public SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile, MultipartFile file) {
        Smartphone smartphoneFromDB = getSmartphoneById(id);
        Smartphone smartphoneFromDTO = dtoConverter.convertToEntity(smartphoneProfile, Smartphone.class);

        smartphoneFromDTO.setId(id);
        smartphoneFromDTO.setCategory(categoryService.getCategoryById(smartphoneProfile.getCategoryId()));
        smartphoneFromDTO.setIsDiscountActive(smartphoneFromDB.getIsDiscountActive());
        smartphoneFromDTO.setDiscountPercent(smartphoneFromDB.getDiscountPercent());
        smartphoneFromDTO.setDiscountedPrice(smartphoneFromDB.getDiscountedPrice());
        // FIXME: 01.05.2023 need to delete old image
        addImageToSmartphone(smartphoneFromDTO, file);

        log.debug("smartphone was updated successfully by id '{}'", id);
        return dtoConverter.convertToDTO(smartphoneRepository.save(smartphoneFromDTO), SmartphoneResponse.class);
    }

    private static void addImageToSmartphone(Smartphone smartphone, MultipartFile file) {
        if (!file.isEmpty()) {
            Path uploadDir = Paths.get(uploadPath);

            if (!Files.exists(uploadDir)) {
                try {
                    Files.createDirectories(uploadDir);
                } catch (IOException e) {
                    throw new DirectoryCreationException(String.format(DIRECTORY_CREATION_EXCEPTION, uploadPath));
                }
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                throw new FileTransferException();
            }

            smartphone.setImageName(resultFileName);
        } else {
            smartphone.setImageName("unknown");
        }
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
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(SMARTPHONE_DELETING_ERROR);
        }

        log.debug("smartphone was successfully deleted by id '{}'", id);
        return dtoConverter.convertToDTO(smartphone, SmartphoneResponse.class);
    }
}
