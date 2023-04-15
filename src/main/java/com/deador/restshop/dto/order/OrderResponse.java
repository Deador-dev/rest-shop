package com.deador.restshop.dto.order;

import com.deador.restshop.dto.UserResponse;
import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse implements Convertible {
    private Long id;

    private UserResponse user;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String lastName;

    private String phoneNumber;

    private String townCity;

    private String address;

    private String postcode;

    @NotBlank
    @Email
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String email;

    private String additionalInformation;

    @NotBlank
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String deliveryStatus;

    @NotNull
    @Min(1)
    @Max(999999)
    private Double totalAmount;
}
