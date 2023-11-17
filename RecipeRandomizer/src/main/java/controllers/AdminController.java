package controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PreAuthorize("isAuthenticated")
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage() {
        return "/WEB-INF/views/admin_main.jsp";
    }
}
