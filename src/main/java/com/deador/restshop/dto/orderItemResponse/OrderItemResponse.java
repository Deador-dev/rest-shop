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
    private SmartphoneResponse smartphone;
}
