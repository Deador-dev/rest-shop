package com.deador.restshop.dto.smartphone;

import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.dto.marker.Convertible;
import com.deador.restshop.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmartphoneResponse implements Convertible {
    private Long id;
    //    @NotBlank
    private String name;

    private CategoryResponse category;
    private Double price;
    private String brand;
    private String model;
    private String operatingSystem;
    private String screenSize;
    private String displayResolution;
    private String matrixType;
    private String processor;
    private String numberOfCores;
    private String battery;
    private String ram;
    private String storageCapacity;
    private String numberOfMainCameras;
    private String mainCameraResolution;
    private String numberOfFrontCameras;
    private String frontCameraResolution;
    private String numberOfSimCards;
    private String connectivity;
    private String bluetooth;
    private String nfc;
    //    @NotBlank
//    @Min(1)
//    @Max(999999)
    private Double weight;
    private Boolean isPromotionActive;
    private Double priceBeforePromotion;
    private Double priceAfterPromotion;
    //    @NotBlank
//    @Size(min = 50, max = 1500, message = "Description should be between 50 and 1500 chars")
    private String description;
    // FIXME: 14.04.2023 @Size?
    private String imageName;
    private Long countOfViews;
}
