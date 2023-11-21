package controllers;

import exceptions.LoginAndRegistrationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import persistence.dao.services.interfaces.IUserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String getSome(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        String username1 = userDetails.getUsername();

        Principal principal = request.getUserPrincipal();
        String username2 = principal.getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username3 = authentication.getName();

        return "/WEB-INF/views/_.jsp";
    }


    @GetMapping(value = "/registration")
    public String getRegistration(Model model) {
        return "/WEB-INF/views/registration.jsp";
    }

    @PostMapping(value = "/registration")
    public String postRegistration(@ModelAttribute("userDTO") UserDTO userDTO, RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            redirectAttributes.addFlashAttribute("message", userService.registrationNewUser(userDTO));
            return "redirect:/user/login";
        } catch (LoginAndRegistrationException e) {
            model.addAttribute("error", e.getMessage());
            return "/WEB-INF/views/registration.jsp";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(name = "error", required = false) String error,
                                @RequestParam(name = "message", required = false) String message,
                                @RequestParam(name = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Невірний логін чи пароль :(");
        }
        if (logout != null) {
            model.addAttribute("message", "Введіть логін і пароль");
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "/WEB-INF/views/login.jsp";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login?logout";
    }

    @GetMapping(value = "/access-denied")
    public String getAccessDenied() {
        return "/WEB-INF/views/access_denied.jsp";
    }
}
