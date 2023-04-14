package com.deador.restshop.dto.smartphone;

import com.deador.restshop.dto.marker.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartphoneProfile implements Convertible {
    private Long id;

    @NotBlank
    @Size(min = 5, max = 100, message = "Name should be between 5 and 100 chars")
    private String name;

    private Long categoryId;

    @NotBlank
    @Min(1)
    @Max(999999)
    private Double price;

    @NotBlank
    @Size(min = 1, max = 100, message = "Brand should be between 1 and 100 chars")
    private String brand;

    @NotBlank
    @Size(min = 1, max = 100, message = "Model should be between 1 and 100 chars")
    private String model;

    @NotBlank
    @Size(min = 1, max = 100, message = "Operating system should be between 1 and 100 chars")
    private String operatingSystem;

    @NotBlank
    @Size(min = 1, max = 100, message = "Screen size should be between 1 and 100 chars")
    private String screenSize;

    @NotBlank
    @Size(min = 1, max = 100, message = "Display resolution size should be between 1 and 100 chars")
    private String displayResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "Matrix type should be between 1 and 100 chars")
    private String matrixType;

    @NotBlank
    @Size(min = 1, max = 100, message = "Processor should be between 1 and 100 chars")
    private String processor;

    @NotBlank
    @Size(min = 1, max = 100, message = "Number of cores should be between 1 and 100 chars")
    private String numberOfCores;

    @NotBlank
    @Size(min = 1, max = 100, message = "Battery should be between 1 and 100 chars")
    private String battery;

    @NotBlank
    @Size(min = 1, max = 100, message = "RAM should be between 1 and 100 chars")
    private String ram;

    @NotBlank
    @Size(min = 1, max = 100, message = "Storage capacity should be between 1 and 100 chars")
    private String storageCapacity;

    @NotBlank
    @Size(min = 1, max = 100, message = "Number of main cameras should be between 1 and 100 chars")
    private String numberOfMainCameras;

    @NotBlank
    @Size(min = 1, max = 100, message = "Main camera resolution should be between 1 and 100 chars")
    private String mainCameraResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "Number of front cameras should be between 1 and 100 chars")
    private String numberOfFrontCameras;


    @NotBlank
    @Size(min = 1, max = 100, message = "Front camera resolution should be between 1 and 100 chars")
    private String frontCameraResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "Number of sim cards should be between 1 and 100 chars")
    private String numberOfSimCards;

    @NotBlank
    @Size(min = 1, max = 100, message = "Connectivity should be between 1 and 100 chars")
    private String connectivity;

    @NotBlank
    @Size(min = 1, max = 100, message = "Bluetooth should be between 1 and 100 chars")
    private String bluetooth;

    @NotBlank
    @Size(min = 1, max = 100, message = "NFC should be between 1 and 100 chars")
    private String nfc;

    @NotBlank
    @Min(1)
    @Max(999999)
    private Double weight;

    private Boolean isPromotionActive;

    @Min(1)
    @Max(999999)
    private Double priceBeforePromotion;

    @Min(1)
    @Max(999999)
    private Double priceAfterPromotion;

    @NotBlank
    @Size(min = 50, max = 1500, message = "Description should be between 50 and 1500 chars")
    private String description;

    // FIXME: 14.04.2023 @Size?
    private String imageName;

    @Min(0)
    @Max(999999)
    private Long countOfViews;
}
