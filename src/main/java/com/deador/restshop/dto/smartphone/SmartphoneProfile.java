package com.deador.restshop.dto.smartphone;

import com.deador.restshop.dto.marker.Convertible;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartphoneProfile implements Convertible {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100, message = "should be between 2 and 100 chars")
    private String name;

    @NotNull
    private Long categoryId;

    @NotNull
    @Min(1)
    @Max(999999)
    private Double price;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String brand;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String model;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String operatingSystem;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String screenSize;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String displayResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String matrixType;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String processor;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String numberOfCores;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String battery;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String ram;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String storageCapacity;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String numberOfMainCameras;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String mainCameraResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String numberOfFrontCameras;


    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String frontCameraResolution;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String numberOfSimCards;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String connectivity;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String bluetooth;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String nfc;

    @NotNull
    @Min(1)
    @Max(999999)
    private Double weight;

    @NotBlank
    @Size(min = 50, max = 1500, message = "should be between 50 and 1500 chars")
    private String description;

    // FIXME: 14.04.2023 @Size?
    private String imageName;

    @Min(0)
    @Max(999999)
    private Long countOfViews;
}
