package com.deador.restshop.service;

import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.dto.smartphone.UpdateSmartphoneIsDiscountActive;
import com.deador.restshop.model.Smartphone;

import java.util.List;

public interface SmartphoneService {
    List<SmartphoneResponse> getAllSmartphoneResponses();

    List<Smartphone> getAllSmartphonesByCategoryId(Long id);

    Smartphone getSmartphoneById(Long id);

    SmartphoneResponse getSmartphoneResponseById(Long id);

    SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile);

    SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile);

    SmartphoneResponse updateIsDiscountActiveById(Long id, UpdateSmartphoneIsDiscountActive updateSmartphoneIsDiscountActive);

    SmartphoneResponse deleteSmartphoneById(Long id);
}
