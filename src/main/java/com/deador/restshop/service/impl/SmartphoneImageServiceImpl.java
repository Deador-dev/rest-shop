package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.smartphoneImage.SmartphoneImageResponse;
import com.deador.restshop.exception.*;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.model.SmartphoneImage;
import com.deador.restshop.repository.SmartphoneImageRepository;
import com.deador.restshop.service.SmartphoneImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class SmartphoneImageServiceImpl implements SmartphoneImageService {
    private final static String uploadPath = System.getProperty("user.dir") + "/src/main/resources/static/smartphoneImages/";
    private static final String DIRECTORY_CREATION_EXCEPTION = "Directory creation error with path: ";
    private static final String SMARTPHONE_IMAGE_NOT_FOUND_BY_ID = "Smartphone image not found by id: %s";
    private static final String SMARTPHONE_IMAGE_DELETING_ERROR = "Can't delete smartphone image cause of relationships";
    private static final String SMARTPHONE_IMAGE_DELETE_FAILURE = "Smartphone image delete failure with path name: %s";
    private final SmartphoneImageRepository smartphoneImageRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public SmartphoneImageServiceImpl(SmartphoneImageRepository smartphoneImageRepository,
                                      DTOConverter dtoConverter) {
        this.smartphoneImageRepository = smartphoneImageRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<SmartphoneImageResponse> getListOfSmartphoneImageResponsesBySmartphoneId(Long id) {
        List<SmartphoneImage> smartphoneImages = smartphoneImageRepository.findAllBySmartphoneId(id);

        log.debug("get list of smartphone images by smartphone id '{}'", id);
        return smartphoneImages.stream()
                .map(smartphoneImage -> (SmartphoneImageResponse) dtoConverter.convertToDTO(smartphoneImage, SmartphoneImageResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSmartphoneImageById(Long id) {
        SmartphoneImage smartphoneImage = smartphoneImageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("smartphone image not found by id '{}'", id);
                    throw new NotExistException(String.format(SMARTPHONE_IMAGE_NOT_FOUND_BY_ID, id));
                });
        try {
            String pathName = uploadPath + smartphoneImage.getImageName();
            File file = new File(pathName);

            if (file.delete()) {
                smartphoneImageRepository.deleteById(id);
                smartphoneImageRepository.flush();
            } else {
                log.error("smartphone image delete failure with path '{}'", pathName);
                throw new FileDeleteFailureException(String.format(SMARTPHONE_IMAGE_DELETE_FAILURE, pathName));
            }
        } catch (DataAccessException | ValidationException exception) {
            log.error("can't delete smartphone image cause of relationships by id '{}'", id);
            throw new DatabaseRepositoryException(SMARTPHONE_IMAGE_DELETING_ERROR);
        }

        log.debug("smartphone image was successfully deleted by id '{}'", id);
    }

    @Override
    public void addSmartphoneImage(SmartphoneImage smartphoneImage) {
        smartphoneImageRepository.save(smartphoneImage);
    }

    @Override
    public void addImagesToSmartphone(Smartphone smartphone, MultipartFile[] images) {
        if (images != null && images.length > 0 && images[0] != null && !images[0].getOriginalFilename().isEmpty()) {
            Path uploadDir = Paths.get(uploadPath);

            if (!Files.exists(uploadDir)) {
                try {
                    Files.createDirectories(uploadDir);
                } catch (IOException e) {
                    throw new DirectoryCreationException(String.format(DIRECTORY_CREATION_EXCEPTION, uploadPath));
                }
            }

            for (MultipartFile image : images) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + image.getOriginalFilename();

                try {
                    image.transferTo(new File(uploadPath + "/" + resultFileName));
                } catch (IOException e) {
                    throw new FileTransferException();
                }

                SmartphoneImage smartphoneImage = SmartphoneImage.builder().smartphone(smartphone).imageName(resultFileName).build();
                addSmartphoneImage(smartphoneImage);
            }

            log.debug("smartphone images was added successfully to smartphone by '{}'", smartphone.getId());
        }
    }
}
