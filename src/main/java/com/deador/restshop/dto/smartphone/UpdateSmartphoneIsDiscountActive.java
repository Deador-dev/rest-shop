package com.deador.restshop.dto.smartphone;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateSmartphoneIsDiscountActive implements Convertible {
    @NotNull
    private Boolean isDiscountActive;

    @NotNull
    @Min(0)
    @Max(99)
    private Double discountPercent;
}
