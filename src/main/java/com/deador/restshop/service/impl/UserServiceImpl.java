package com.deador.restshop.service.impl;

import com.deador.restshop.constants.RoleData;
import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.user.SuccessLogin;
import com.deador.restshop.dto.user.UserLogin;
import com.deador.restshop.dto.user.UserProfile;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.model.User;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.exception.NotVerifiedUserException;
import com.deador.restshop.exception.UserAuthenticationException;
import com.deador.restshop.repository.RoleRepository;
import com.deador.restshop.repository.UserRepository;
import com.deador.restshop.security.JwtUtils;
import com.deador.restshop.service.CartService;
import com.deador.restshop.service.MailSenderService;
import com.deador.restshop.service.RefreshTokenService;
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
    private static final String USER_NOT_FOUND_BY_EMAIL = "User not found by email: %s";
    private static final String USER_NOT_FOUND_BY_VERIFICATION_CODE = "User not found by verification code: %s";
    private static final String USER_ALREADY_EXIST_WITH_EMAIL = "User already exist with email: %s";
    private static final String USER_DELETING_ERROR = "Can't delete user";
    private static final String ROLE_NOT_FOUND_BY_ROLE_NAME = "Role not found by role name: %s";
    private static final String WRONG_PASSWORD = "Wrong password";
    private static final String NOT_VERIFIED = "You have not verified your email: %s";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartService cartService;
    private final MailSenderService mailSenderService;
    private final RefreshTokenService refreshTokenService;
    private final DTOConverter dtoConverter;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CartService cartService,
                           MailSenderService mailSenderService,
                           RefreshTokenService refreshTokenService,
                           DTOConverter dtoConverter,
                           JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartService = cartService;
        this.mailSenderService = mailSenderService;
        this.refreshTokenService = refreshTokenService;
        this.dtoConverter = dtoConverter;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUserResponses() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(user -> (UserResponse) dtoConverter.convertToDTO(user, UserResponse.class))
                .collect(Collectors.toList());
        log.debug("get list of users '{}'", userResponses);

        return userResponses;
    }

    @Override
    public List<UserResponse> getUserResponsesByRole(String roleName) {
        List<User> users = userRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    log.error("users not found by role name '{}'", roleName);
                    return new NotExistException(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, roleName));
                });

        if (users.isEmpty()) {
            log.error("users not found by role name '{}'", roleName);
            throw new NotExistException(String.format(USERS_NOT_FOUND_BY_ROLE_NAME, roleName));
        }

        List<UserResponse> userResponses = users.stream()
                .map(user -> (UserResponse) dtoConverter.convertToDTO(user, UserResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of user responses '{}' by role name '{}'", userResponses, roleName);
        return userResponses;
    }

    @Override
    public User getUserById(Long id) {
        log.debug("get user by id '{}'", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("user not found by id '{}'", id);
                    return new NotExistException(String.format(USER_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public User getUserByEmail(String email) {
        log.debug("get user by email '{}'", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("user not found by email '{}'", email);
                    return new NotExistException(String.format(USER_NOT_FOUND_BY_EMAIL, email));
                });
    }

    @Override
    public User getUserByVerificationCode(String verificationCode) {
        log.debug("get user by verificationCode '{}'", verificationCode);
        return userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> {
                    log.error("user not found by verification code '{}'", verificationCode);
                    return new NotExistException(String.format(USER_NOT_FOUND_BY_VERIFICATION_CODE, verificationCode));
                });
    }

    @Override
    public UserResponse getUserResponseById(Long id) {
        log.debug("get user response by id '{}'", id);
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
                    log.error("role not found by role name '{}'", RoleData.USER.getDBRoleName());
                    return new NotExistException(String.format(ROLE_NOT_FOUND_BY_ROLE_NAME, RoleData.USER.getDBRoleName()));
                })));

        user.setPassword(passwordEncoder.encode(userProfile.getPassword()));
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setStatus(false);
        user = userRepository.save(user);
        cartService.createCartForUser(user);

        mailSenderService.sendVerificationMessage(user);

        log.debug("user with email '{}' was registered successfully", user.getEmail());
        return dtoConverter.convertToDTO(user, UserResponse.class);
    }

    @Override
    public UserResponse verify(String verificationCode) {
        User user = getUserByVerificationCode(verificationCode);

        user.setStatus(true);
        user.setVerificationCode(null);
        user = userRepository.save(user);

        log.debug("user with email '{}' was verified successfully", user.getEmail());
        return dtoConverter.convertToDTO(user, UserResponse.class);
    }

    @Override
    public SuccessLogin loginUser(UserLogin userLogin) {
        userLogin.setEmail(userLogin.getEmail().toLowerCase());
        User user = getUserByEmail(userLogin.getEmail());
        if (!user.getStatus()) {
            throw new NotVerifiedUserException(String.format(NOT_VERIFIED, userLogin.getEmail()));
        } else if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw new UserAuthenticationException(WRONG_PASSWORD);
        }

        log.debug("user '{}' with email '{}' logged successfully", userLogin, userLogin.getEmail());
        return SuccessLogin.builder()
                .id(user.getId())
                .email(user.getEmail())
                .accessToken(jwtUtils.generateAccessToken(userLogin.getEmail()))
                .refreshToken(refreshTokenService.assignRefreshToken(user))
                .build();
    }
}