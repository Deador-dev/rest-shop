package com.deador.restshop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "smartphones")
public class Smartphone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private Double price;

    @Column
    private String brand;

    @Column
    private String model;

    @Column
    private String operatingSystem;

    @Column
    private String screenSize;

    @Column
    private String displayResolution;

    @Column
    private String matrixType;

    @Column
    private String processor;

    @Column
    private String numberOfCores;

    @Column
    private String battery;

    @Column
    private String ram;

    @Column
    private String storageCapacity;

    @Column
    private String numberOfMainCameras;

    @Column
    private String mainCameraResolution;

    @Column
    private String numberOfFrontCameras;

    @Column
    private String frontCameraResolution;

    @Column
    private String numberOfSimCards;

    @Column
    private String connectivity;

    @Column
    private String bluetooth;

    @Column
    private String nfc;

    @Column
    private Double weight;

    @Column
    private Boolean isPromotionActive;

    @Column
    private Double priceBeforePromotion;

    @Column
    private Double priceAfterPromotion;

    @Column
    private String description;

    @Column
    private String imageName;

    @Column
    private Long countOfViews;
}
