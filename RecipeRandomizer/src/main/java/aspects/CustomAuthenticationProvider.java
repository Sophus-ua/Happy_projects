package aspects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import persistence.dao.services.interfaces.IUserService;



@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final IUserService userService;

//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public CustomAuthenticationProvider(IUserService userService, PasswordEncoder passwordEncoder) {
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Autowired
    public CustomAuthenticationProvider(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userService.loadUserByUsername(username);

//        if (passwordEncoder.matches(password, userDetails.getPassword())) {
        if (userDetails.getPassword().equals(password)) {
            return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Введено невірні данні");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

