package com.deador.restshop.dto.smartphone;

import com.deador.restshop.dto.category.CategoryResponse;
import com.deador.restshop.dto.marker.Convertible;
import com.deador.restshop.dto.smartphoneImage.SmartphoneImageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmartphoneResponse implements Convertible {
    private Long id;
    @NotBlank(message = "cannot be empty")
    @Size(min = 2, max = 100, message = "should be between 2 and 100 chars")
    private String name;
    private CategoryResponse category;
    private List<SmartphoneImageResponse> smartphoneImages;
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
    @NotNull(message = "cannot be null")
    @Min(1)
    @Max(999999)
    private Double weight;
    private Boolean isDiscountActive;
    private Double discountPercent;
    private Double discountedPrice;
    @NotBlank
    @Size(min = 50, max = 1500, message = "should be between 50 and 1500 chars")
    private String description;
    @Min(0)
    @Max(999999)
    private Long countOfViews;
}
