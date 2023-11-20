package controllers;


import exceptions.DatabaseUpdateException;
import exceptions.EmptyRecipeIdsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import models.CustomTagDTO;
import models.ImageBufferDTO;
import models.RecipeDTO;
import models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import persistence.dao.services.interfaces.*;
import persistence.entity.Recipe;

import java.util.List;


@Controller
@PreAuthorize("isAuthenticated")
public class RecipeRandomizerController {
    private ISearchFormService searchFormService;
    private IRecipeService recipeService;
    private IDTOService dtoService;
    private IRecipeDesignerService recipeDesignerService;
    private IRecipeImageCacheService recipeImageCacheService;
    private ICustomTagsService customTagsService;


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage(@ModelAttribute("message") String message,
                           HttpServletRequest request, HttpSession session,
                           Model model) {
        if (message != null)
            model.addAttribute("message", message);
        String sessionKey = request.getUserPrincipal().getName() + "recipeIds";
        session.removeAttribute(sessionKey);
        return "/WEB-INF/views/main.jsp";
    }


    @GetMapping(value = "/tag-handler")
    public String tagHandler() {
        return "/WEB-INF/views/tag_handler.jsp";
    }

    @PostMapping(value = "/delete-tag")
    public String deleteTag(@ModelAttribute("customTagDTO") CustomTagDTO customTagDTO, Model model,
                            HttpServletRequest request) {
        customTagDTO.setUsername(request.getUserPrincipal().getName());
        String result = customTagsService.deleteCustomTagByIdForUser(customTagDTO);
        model.addAttribute("message", result);
        return "/WEB-INF/views/tag_handler.jsp";
    }

    @PostMapping(value = "/add-tag")
    public String addTag(@ModelAttribute("customTagDTO") CustomTagDTO customTagDTO, Model model,
                         HttpServletRequest request) {
        customTagDTO.setUsername(request.getUserPrincipal().getName());
        String result = customTagsService.addCustomTag(customTagDTO);
        model.addAttribute("message", result);
        return "/WEB-INF/views/tag_handler.jsp";
    }

    @GetMapping(value = "/recipe-handler")
    public String addRecipe(@ModelAttribute("message") String message, Model model) {
        if (message != null)
            model.addAttribute("message", message);

        return "/WEB-INF/views/recipe_handler.jsp";
    }

    @PostMapping(value = "/recipe-handler")
    public String updateRecipe(@RequestParam(name = "recipeID") long recipeID, Model model) {
        RecipeDTO recipeDTO = dtoService.findRecipeDTO(recipeID);
        System.out.println("RecipeRandomizerController.updateRecipe recipeDTO: " + recipeDTO);
        model.addAttribute("recipeDTOJson", recipeDesignerService.parseRecipeDTOtoJson(recipeDTO));
        return "/WEB-INF/views/recipe_handler.jsp";
    }

    @PostMapping(value = "/recipe-add-or-update")
    public String recipeAddOrUpdate(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO, Model model,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String username = request.getUserPrincipal().getName();
        recipeDTO.setUsername(username);

        try {
            recipeDTO = recipeDesignerService.saveRecipeByRecipeDTO(recipeDTO);
        } catch (DatabaseUpdateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/main";
        }

//        String message = recipeImageCacheService.updateImageFromCacheWithDelay(recipeDTO);

        ImageBufferDTO imageDTO = new ImageBufferDTO(recipeDTO.getId(), recipeDTO.getName(), recipeDTO.getUsername());
        String message = recipeImageCacheService.updateImageFromBufferWithDelay(imageDTO);

        if (recipeDTO.getId() != null) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                System.out.println("RecipeRandomizerController.recipeAddOrUpdate error when try sleep");
            }
//            return getRecipeById(recipeDTO.getId(), model, redirectAttributes);
            return "redirect:/recipe/" + recipeDTO.getId();
        } else {
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/main";
        }
    }

    @PostMapping(value = "/delete-recipe")
    public String deleteRecipe(@RequestParam(name = "recipeID") Long recipeID, Model model,
                               HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        String result = recipeService.deleteByIdAndUsername(recipeID, username);
        model.addAttribute("message", result);
        return "/WEB-INF/views/main.jsp";
    }

    @GetMapping(value = "/recipes-found")
    public String recipesFound(HttpServletRequest request, HttpSession session,
                               Model model, RedirectAttributes redirectAttributes,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "20") int size) {
        String username = request.getUserPrincipal().getName();
        String sessionKey = username + "recipeIds";

        try {
            Object sessionAttribute = session.getAttribute(sessionKey);
            if (!(sessionAttribute instanceof List<?>))
                throw new EmptyRecipeIdsException("Рецептів з такими параметрами не знайдено");

            List<?> rawList = (List<?>) sessionAttribute;
            if (rawList.isEmpty() || !(rawList.get(0) instanceof Long))
                throw new EmptyRecipeIdsException("Рецептів з такими параметрами не знайдено");

            List<Long> recipesIds = (List<Long>) rawList;
            Page<Recipe> recipesPage = searchFormService.findPageOfRecipesByIds(recipesIds, page, size);

            model.addAttribute("recipesUsername", recipesPage.stream().findAny().get().getUser().getUsername());
            model.addAttribute("recipes", recipesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", recipesPage.getTotalPages());
            return "/WEB-INF/views/recipes_found.jsp";
        } catch (EmptyRecipeIdsException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/main";
        }
    }


    @GetMapping(value = "/search-recipes-name-like")
    public String searchRecipesNameLike(@RequestParam(name = "recipesNameLike", required = false) String recipeName,
                                        HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        if (recipeName == null || recipeName.isEmpty())
            return "redirect:/main";

        String username = request.getUserPrincipal().getName();

        List<Long> recipeIds;
        try {
            recipeIds = recipeService.findRecipeIdsByNameLikeForUser(recipeName, username);
        } catch (EmptyRecipeIdsException e){
            redirectAttributes.addFlashAttribute("message", e.getCustomMessage());
            return "redirect:/main";
        }

        String sessionKey = username + "recipeIds";
        session.setAttribute(sessionKey, recipeIds);
        return "redirect:/recipes-found";
    }

    @GetMapping(value = "/search-recipes-by-form")
    public String searchRecipesIdsByForm(@ModelAttribute("searchModel") SearchModel searchModel, HttpSession session,
                                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        List<Long> recipeIds;
        try {
            recipeIds = searchFormService.findRecipeIdsForUserBySearchModel(searchModel, username);
        } catch (EmptyRecipeIdsException e) {
            redirectAttributes.addFlashAttribute("message", e.getCustomMessage());
            return "redirect:/main";
        }

        String sessionKey = username + "recipeIds";
        session.setAttribute(sessionKey, recipeIds);

        return "redirect:/recipes-found";
    }

    @GetMapping(value = "/recipe-search-randomizer")
    public String recipeSearchRandomizer(@ModelAttribute("searchModel") SearchModel searchModel, Model model,
                                         HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String username = request.getUserPrincipal().getName();
        String recipesIdsJson;
        try {
            recipesIdsJson = searchFormService.getRandomizeRecipeIdsJSON(searchModel, username);
        } catch (EmptyRecipeIdsException e){
            redirectAttributes.addFlashAttribute("message", e.getCustomMessage());
            return "redirect:/main";
        }
        model.addAttribute("recipesIdsJson", recipesIdsJson);
        System.out.println("/recipe-search-randomizer  recipesIdsJson.length: " + recipesIdsJson.length());
        return "/WEB-INF/views/recipe_randomizer.jsp";
    }

    @GetMapping(value = "/recipe/{id}")
    public String getRecipeById(@PathVariable Long id, Model model, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        String username = request.getUserPrincipal().getName();
        RecipeDTO recipeDTO = dtoService.findRecipeDTOForUser(id, username);

        if (recipeDTO == null) {
            redirectAttributes.addFlashAttribute("message", "Рецепт не знайдено");
            return "redirect:/main";
        }

        model.addAttribute("recipeDTO", recipeDTO);
        return "/WEB-INF/views/recipe.jsp";
    }

    @PostMapping(value = "/add-recipe-to-mine")
    public String addRecipeToMine(@RequestParam(name = "recipeID") long recipeID, HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
        if (recipeID == 0) {
            redirectAttributes.addFlashAttribute("message", "НЕ вдалось додати рецепт");
            return "redirect:/main";
        }

        Long newRecipeId;
        try {
            newRecipeId = recipeService.copyRecipeToUserById(request.getUserPrincipal().getName(), recipeID);
        } catch (DatabaseUpdateException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/main";
        }

        return "redirect:/recipe/" + newRecipeId;
    }


    @Autowired
    public void setRecipeService(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setSearchFormService(ISearchFormService searchFormService) {
        this.searchFormService = searchFormService;
    }

    @Autowired
    public void setRegionalCuisineService(IDTOService dtoService) {
        this.dtoService = dtoService;
    }

    @Autowired
    public void setRecipeDesignerService(IRecipeDesignerService recipeDesignerService) {
        this.recipeDesignerService = recipeDesignerService;
    }

    @Autowired
    public void setRecipeImageCacheService(IRecipeImageCacheService recipeImageCacheService) {
        this.recipeImageCacheService = recipeImageCacheService;
    }

    @Autowired
    public void setCustomTagsService(ICustomTagsService customTagsService) {
        this.customTagsService = customTagsService;
    }
}
