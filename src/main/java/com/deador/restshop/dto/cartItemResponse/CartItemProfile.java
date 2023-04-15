package com.deador.restshop.dto.cartItemResponse;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemProfile implements Convertible {
    private Long id;

    private Long cartId;

    private Long smartphoneId;
}
