package com.deador.restshop.service;

import com.deador.restshop.dto.user.SuccessLogin;
import com.deador.restshop.dto.user.UserLogin;
import com.deador.restshop.dto.user.UserProfile;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.model.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUserResponses();

    List<UserResponse> getUserResponsesByRole(String roleName);

    User getUserById(Long id);

    User getUserByEmail(String email);

    User getUserByVerificationCode(String verificationCode);

    UserResponse getUserResponseById(Long id);

    UserResponse registerUser(UserProfile userProfile);

    UserResponse verify(String verificationCode);

    SuccessLogin loginUser(UserLogin userLogin);
}
