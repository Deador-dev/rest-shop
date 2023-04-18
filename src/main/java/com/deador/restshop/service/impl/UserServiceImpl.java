package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.UserResponse;
import com.deador.restshop.entity.User;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.RoleRepository;
import com.deador.restshop.repository.UserRepository;
import com.deador.restshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USERS_NOT_FOUND_BY_ROLE_NAME = "Users not found by roleName: %s";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id: %s";
    private static final String USER_ALREADY_EXIST_WITH_EMAIL = "User already exist with email: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user";
    private final UserRepository userRepository;
    private final DTOConverter dtoConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           DTOConverter dtoConverter) {
        this.userRepository = userRepository;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<UserResponse> getAllUserResponses() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(user -> (UserResponse) dtoConverter.convertToDTO(user, UserResponse.class))
                .collect(Collectors.toList());
        log.debug("getting list of users {}", userResponses);

        return userResponses;
    }

    @Override
    public List<UserResponse> getUserResponsesByRole(String roleName) {
        List<User> users = userRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    log.error("users not found by role name {}", roleName);
                    return new NotExistException(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, roleName));
                });

        if (users.isEmpty()) {
            log.error("users not found by role name {}", roleName);
            throw new NotExistException(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, roleName));
        }

        List<UserResponse> userResponses = users.stream()
                .map(user -> (UserResponse) dtoConverter.convertToDTO(user, UserResponse.class))
                .collect(Collectors.toList());

        log.debug("getting list of users by role name {}", userResponses);
        return userResponses;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("user not found by id {}", id);
                    return new NotExistException(String.format(USER_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public UserResponse getUserResponseById(Long id) {
        return dtoConverter.convertToDTO(getUserById(id), UserResponse.class);
    }

}
