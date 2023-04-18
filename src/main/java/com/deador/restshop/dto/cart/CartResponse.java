package com.deador.restshop.dto.cart;

import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartResponse implements Convertible {
    private Long id;

    private UserResponse user;
    private List<CartItemResponse> cartItems;
    @NotNull(message = "cannot be null")
    @Min(1)
    @Max(100)
    private Integer quantity;
    @NotNull(message = "cannot be null")
    @Min(1)
    @Max(999999)
    private Double price;
}
