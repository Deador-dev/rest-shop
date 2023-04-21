package com.deador.restshop.service;

import com.deador.restshop.dto.user.UserProfile;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUserResponses();

    List<UserResponse> getUserResponsesByRole(String roleName);

    User getUserById(Long id);

    User getUserByVerificationCode(String verificationCode);

    UserResponse getUserResponseById(Long id);

    UserResponse registerUser(UserProfile userProfile);

    UserResponse verify(String verificationCode);
}
