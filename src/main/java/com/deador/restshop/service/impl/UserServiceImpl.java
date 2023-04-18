package com.deador.restshop.service.impl;

import com.deador.restshop.constants.RoleData;
import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.user.UserProfile;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.entity.User;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.RoleRepository;
import com.deador.restshop.repository.UserRepository;
import com.deador.restshop.service.CartService;
import com.deador.restshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private static final String USERS_NOT_FOUND_BY_ROLE_NAME = "Users not found by roleName: %s";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id: %s";
    private static final String USER_ALREADY_EXIST_WITH_EMAIL = "User already exist with email: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user";
    private static final String ROLE_NOT_FOUND_BY_ROLE_NAME = "Role not found by role name: %s";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartService cartService;
    private final DTOConverter dtoConverter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CartService cartService,
                           DTOConverter dtoConverter,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartService = cartService;
        this.dtoConverter = dtoConverter;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public UserResponse registerUser(UserProfile userProfile) {
        userProfile.setEmail(userProfile.getEmail().toLowerCase());

        if (userRepository.existsByEmail(userProfile.getEmail())) {
            throw new AlreadyExistException(String.format(USER_ALREADY_EXIST_WITH_EMAIL, userProfile.getEmail()));
        }

        User user = dtoConverter.convertToEntity(userProfile, User.class);

        user.setRoles(Collections.singletonList(roleRepository.findByName(RoleData.USER.getDBRoleName())
                .orElseThrow(() -> {
                    log.error("role not found by role name {}", RoleData.USER.getDBRoleName());
                    return new NotExistException(String.format(ROLE_NOT_FOUND_BY_ROLE_NAME, RoleData.USER.getDBRoleName()));
                })));

        user.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        user.setVerificationCode(UUID.randomUUID().toString());
        // FIXME: 18.04.2023 need to set user.setStatus(false);
        user.setStatus(true);
        user = userRepository.save(user);
        cartService.createCartForUser(user);
        log.debug("user {} was registered successfully", user);
        // TODO: 18.04.2023 need to create sendVerificationEmail with MailSend

        return dtoConverter.convertToDTO(user, UserResponse.class);
    }

}
