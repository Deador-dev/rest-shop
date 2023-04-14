package com.deador.restshop.dto.category;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryProfile implements Convertible {
    private Long id;

    @NotBlank
    // FIXME: 14.04.2023 or @NotEmpty?
    private String name;
}
