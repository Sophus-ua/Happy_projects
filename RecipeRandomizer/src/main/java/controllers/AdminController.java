package controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import persistence.dao.services.interfaces.IDTOService;
import persistence.dao.services.interfaces.IUserService;


@Controller
@PreAuthorize("isAuthenticated")
@RequestMapping("/admin")
public class AdminController {
    private IDTOService dtoService;
    private IUserService userService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage() {
        return "/WEB-INF/views/admin_main.jsp";
    }

    @GetMapping(value = "/user-handler")
    public String getAllUsers(@ModelAttribute("message") String message,
                              Model model) {
        if (message != null)
            model.addAttribute("message", message);
        model.addAttribute("users", dtoService.findAllUsers());
        return "/WEB-INF/views/all_users.jsp";
    }

    @GetMapping(value = "/change-activity-status/{userId}")
    public String changeUserActivityStatus(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        String message = userService.changeUserActivityStatus(userId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/user-handler";
    }

    @GetMapping(value = "/delete-user/{userId}")
    public String deleteUser(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
        String message = userService.deleteUserById(userId);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/user-handler";
    }



    @Autowired
    public void setDtoService(IDTOService dtoService){
        this.dtoService = dtoService;
    }
    @Autowired
    public void setUserService(IUserService userService){
        this.userService = userService;
    }
}
