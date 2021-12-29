package gr.hua.ds_group_13;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

        @Component public class AuthProvider implements AuthenticationProvider {
            @Autowired private SecurityUserDetailsService userDetailsService;
            @Autowired private PasswordEncoder passwordEncoder;
            @Autowired private UserRepository userRepository;
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                return authentication;
                }

            @Override public boolean supports(Class<?> authentication) {
                return true;
            }
        }