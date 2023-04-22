package com.deador.restshop.dto.user;

import com.deador.restshop.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLogin implements Convertible {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
