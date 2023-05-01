package com.deador.restshop.dto.smartphoneImage;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SmartphoneImageResponse implements Convertible {
    private Long id;
    private String imageName;
}
