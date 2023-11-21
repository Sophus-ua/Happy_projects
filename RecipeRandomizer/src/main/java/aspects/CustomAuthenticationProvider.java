package aspects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import persistence.dao.services.interfaces.IUserService;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final IUserService userService;

//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(IUserService userService
//            , PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (userService == null) {
            throw new InternalAuthenticationServiceException("User service is null");
        }

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new AuthenticationCredentialsNotFoundException("No such user was found");
        }

//        if (userDetails.isEnabled() && passwordEncoder.matches(password, userDetails.getPassword())) {
        if (userDetails.isEnabled() && userDetails.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } else {
            throw new AuthenticationServiceException("no such username or incorrect password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

