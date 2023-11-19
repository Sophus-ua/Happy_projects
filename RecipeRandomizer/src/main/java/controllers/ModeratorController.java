package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import persistence.dao.services.interfaces.IRecipeService;
import persistence.dao.services.interfaces.ISearchFormService;

import java.util.List;


@Controller
@PreAuthorize("isAuthenticated")
@RequestMapping("/common")
public class ModeratorController {
    private IRecipeService recipeService;
    private ISearchFormService searchFormService;

    @GetMapping(value = "/search-recipes-name-like")
    public String searchRecipesNameLike(@RequestParam(name = "recipesNameLike", required = false) String recipeName,
                                        HttpServletRequest request, HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        if (recipeName == null || recipeName.isEmpty())
            return "redirect:/main";

        String username = request.getUserPrincipal().getName();
        List<Long> recipeIds = recipeService.findRecipeIdsByNameLikeForUser(recipeName, "Moderator");
        String sessionKey = username + "recipeIds";
        session.setAttribute(sessionKey, recipeIds);

        redirectAttributes.addFlashAttribute("message", "Загальні рецепти");

        return "redirect:/recipes-found";
    }

    @GetMapping(value = "/search-recipes-by-form")
    public String searchRecipesIdsByForm(@ModelAttribute("searchModel") SearchModel searchModel,
                                         HttpServletRequest request, HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        String username = request.getUserPrincipal().getName();
        List<Long> recipeIds = searchFormService.findRecipeIdsForUserBySearchModel(searchModel, "Moderator");
        String sessionKey = username + "recipeIds";
        session.setAttribute(sessionKey, recipeIds);

        redirectAttributes.addFlashAttribute("message", "Загальні рецепти");

        return "redirect:/recipes-found";
    }

    @GetMapping(value = "/recipe-search-randomizer")
    public String recipeSearchRandomizer(@ModelAttribute("searchModel") SearchModel searchModel,
                                         Model model) {
        String recipesIdsJson = searchFormService.getRandomizeRecipeIdsJSON(searchModel, "Moderator");
        model.addAttribute("recipesIdsJson", recipesIdsJson);

        System.out.println("/recipe-search-randomizer  recipesIdsJson.length: " + recipesIdsJson.length());
        return "/WEB-INF/views/recipe_randomizer.jsp";
    }

    @Autowired
    public void setRecipeService(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @Autowired
    public void setSearchFormService (ISearchFormService searchFormService) {
        this.searchFormService = searchFormService;
    }
}
