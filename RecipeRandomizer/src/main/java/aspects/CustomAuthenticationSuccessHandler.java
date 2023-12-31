package aspects;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import persistence.dao.services.interfaces.IUserService;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final IUserService userService;
    private String adminTargetUrl;

    @Autowired
    public CustomAuthenticationSuccessHandler(IUserService userService) {
        this.userService = userService;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return adminTargetUrl;
        }
        return super.determineTargetUrl(request, response, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            updateLastLoginDate(userDetails.getUsername());
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void updateLastLoginDate(String username) {
        userService.updateLastLoginDateByUsername(username);
    }

    public void setAdminTargetUrl(String adminTargetUrl) {
        this.adminTargetUrl = adminTargetUrl;
    }
}
