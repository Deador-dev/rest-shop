package com.deador.restshop.dto.user;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserProfile implements Convertible {
    private Long id;
    @NotBlank
    @Email
    @Size(min = 1, max = 100, message = "should be between 1 and 100 chars")
    private String email;
    @NotBlank
    @Size(min = 3, max = 100, message = "should be between 3 and 100 chars")
    private String password;
    @NotBlank
    @Size(min = 3, max = 30, message = "should be between 3 and 30 chars")
    private String firstName;
    @NotBlank
    @Size(min = 3, max = 30, message = "should be between 3 and 30 chars")
    private String lastName;
    @NotBlank
    @Size(min = 10, max = 20, message = "should be between 10 and 20 chars")
    private String phone;
}
