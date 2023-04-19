package com.deador.restshop.dto.orderItemResponse;

import com.deador.restshop.dto.marker.Convertible;
import com.deador.restshop.dto.smartphone.SmartphoneResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemResponse implements Convertible {
    private Long id;
    // FIXME: 19.04.2023 maybe need to add @JsonIgnore. Reason - infinity recursion
//    private OrderResponse order;
    private SmartphoneResponse smartphone;
}
