package com.deador.restshop.security;


import com.deador.restshop.entity.User;
import com.deador.restshop.exception.UserAuthenticationException;
import com.deador.restshop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final String SECURITY_CONTEXT_HOLDER_ERROR = "The UserPrincipal was not found in the SecurityContextHolder.";
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return UserPrincipal.create(user);
    }

    public UserPrincipal getUserPrincipal() {
        try {
            return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            log.error(SECURITY_CONTEXT_HOLDER_ERROR);
            throw new UserAuthenticationException();
        }
    }
}
