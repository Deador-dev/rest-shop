package com.deador.restshop.dto.category;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryProfile implements Convertible {
    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String name;
}
