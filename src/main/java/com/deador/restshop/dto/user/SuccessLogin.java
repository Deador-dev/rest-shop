package com.deador.restshop.dto.user;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessLogin implements Convertible {
    private Long id;
    private String email;
    private String accessToken;
    private String refreshToken;
}
