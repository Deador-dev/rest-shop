package com.deador.restshop.service;

import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.dto.smartphone.UpdateSmartphoneIsDiscountActive;
import com.deador.restshop.model.Smartphone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SmartphoneService {
    List<SmartphoneResponse> getListOfSmartphoneResponses(Pageable pageable);

    List<Smartphone> getListOfSmartphonesByCategoryId(Long id);

    Page<SmartphoneResponse> getListOfSmartphoneResponsesByCategoryId(Long id, Pageable pageable);

    Page<SmartphoneResponse> getListOfSmartphoneResponsesWithSorting(Pageable pageable);

    Smartphone getSmartphoneById(Long id);

    SmartphoneResponse getSmartphoneResponseById(Long id);

    SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile, MultipartFile[] images);

    SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile, MultipartFile[] images);

    SmartphoneResponse updateIsDiscountActiveById(Long id, UpdateSmartphoneIsDiscountActive updateSmartphoneIsDiscountActive);

    SmartphoneResponse deleteSmartphoneById(Long id);
}
