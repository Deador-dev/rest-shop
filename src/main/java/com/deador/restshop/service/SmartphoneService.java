package com.deador.restshop.service;

import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;

import java.util.List;
import java.util.Optional;

public interface SmartphoneService {
    List<SmartphoneResponse> getAllSmartphones();

    SmartphoneResponse getSmartphoneResponseById(Long id);

    SmartphoneResponse addSmartphone(SmartphoneProfile smartphoneProfile);
}
