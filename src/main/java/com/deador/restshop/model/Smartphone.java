package com.deador.restshop.model;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "smartphones")
public class Smartphone implements Convertible {
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
    @ColumnDefault(value = "false")
    private Boolean isDiscountActive;

    @Column
    @ColumnDefault(value = "0")
    private Double discountPercent;

    @Column
    @ColumnDefault(value = "0")
    private Double discountedPrice;

    @Column
    private String description;

    @Column
    @ColumnDefault(value = "empty")
    private String imageName;

    @Column
    @ColumnDefault(value = "0")
    private Long countOfViews;
}
