package com.deador.restshop.service;

import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.entity.Smartphone;

import java.util.List;
import java.util.Optional;

public interface SmartphoneService {
    List<SmartphoneResponse> getAllSmartphoneResponses();

    List<Smartphone> getAllSmartphonesByCategoryId(Long id);

    Smartphone getSmartphoneById(Long id);

    SmartphoneResponse getSmartphoneResponseById(Long id);

    SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile);

    SmartphoneResponse updateSmartphone(Long id, SmartphoneProfile smartphoneProfile);

    SmartphoneResponse deleteSmartphoneById(Long id);
}
