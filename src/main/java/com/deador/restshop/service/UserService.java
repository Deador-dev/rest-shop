package com.deador.restshop.service;

import com.deador.restshop.dto.UserResponse;
import com.deador.restshop.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUserResponses();

    List<UserResponse> getUserResponsesByRole(String roleName);

    User getUserById(Long id);

    UserResponse getUserResponseById(Long id);
}
