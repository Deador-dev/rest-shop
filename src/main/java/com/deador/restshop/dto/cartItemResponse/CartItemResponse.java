package com.deador.restshop.dto.cartItemResponse;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.dto.marker.Convertible;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemResponse implements Convertible {
    private Long id;
    @JsonIgnore
    private CartResponse cart;

    private SmartphoneResponse smartphone;
}
