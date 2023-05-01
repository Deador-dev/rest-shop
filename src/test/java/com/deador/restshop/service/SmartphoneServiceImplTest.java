package com.deador.restshop.service;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.dto.smartphone.SmartphoneProfile;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.deador.restshop.model.Category;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.repository.CategoryRepository;
import com.deador.restshop.repository.SmartphoneRepository;
import com.deador.restshop.service.impl.CategoryServiceImpl;
import com.deador.restshop.service.impl.SmartphoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SmartphoneServiceImplTest {
    private static final Long EXISTING_SMARTPHONE_ID = 1L;
    private static final Long NOT_EXISTING_SMARTPHONE_ID = 100L;
    private static final String EXISTING_SMARTPHONE_NAME = "iPhone XS";
    private static final String NOT_EXISTING_SMARTPHONE_NAME = "Not existing smartphone name";

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SmartphoneRepository smartphoneRepository;
    @Mock
    private DTOConverter dtoConverter;
    @InjectMocks
    private SmartphoneServiceImpl smartphoneService;
    @InjectMocks
    private CategoryServiceImpl categoryService;


    private Smartphone smartphone;
    private SmartphoneProfile smartphoneProfile;
    private SmartphoneResponse smartphoneResponse;
    private Category category;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void init(){

    }

























}
