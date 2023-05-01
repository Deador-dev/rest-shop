package com.deador.restshop.service;

import com.deador.restshop.dto.smartphoneImage.SmartphoneImageResponse;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.model.SmartphoneImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SmartphoneImageService {
    List<SmartphoneImageResponse> getListOfSmartphoneImageResponsesBySmartphoneId(Long id);

    void deleteSmartphoneImageById(Long id);

    void addSmartphoneImage(SmartphoneImage smartphoneImage);

    void addImagesToSmartphone(Smartphone smartphone, MultipartFile[] images);
}
