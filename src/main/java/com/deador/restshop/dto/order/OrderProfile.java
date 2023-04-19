package com.deador.restshop.dto.order;

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
public class OrderProfile implements Convertible {
    private Long id;

    private Long UserId;

    @NotBlank
    @Email
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String email;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String phoneNumber;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String townCity;

    @NotBlank
    @Size(min = 1, max = 50, message = "should be between 1 and 50 chars")
    private String address;

    @NotBlank
    @Size(min = 1, max = 20, message = "should be between 1 and 20 chars")
    private String postcode;

    @NotBlank
    @Size(min = 1, max = 1500, message = "should be between 1 and 1500 chars")
    private String additionalInformation;
}
