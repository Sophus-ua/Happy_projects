package aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;



public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private String adminTargetUrl;
    private String moderatorTargetUrl;

    public void setAdminTargetUrl(String adminTargetUrl) {
        this.adminTargetUrl = adminTargetUrl;
    }
    public void setModeratorTargetUrl(String moderatorTargetUrl) {
        this.moderatorTargetUrl = moderatorTargetUrl;
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return adminTargetUrl;
        }
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"))) {
            return moderatorTargetUrl;
        }
        return super.determineTargetUrl(request, response, authentication);
    }
}
