package controllers;


import jakarta.servlet.http.HttpServletRequest;
import models.CustomTagDTO;
import models.ImageBufferDTO;
import models.RecipeDTO;
import models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import persistence.dao.services.interfaces.*;


@Controller
@PreAuthorize("isAuthenticated")
public class RecipeRandomizerController {
    private ISearchFormService searchFormService;
    private IRecipeService recipeService;
    private IDTOService dtoService;
    private IRecipeDesignerService recipeDesignerService;
    private IRecipeImageCacheService recipeImageCacheService;
    private ICustomTagsService customTagsService;



    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        recipeService.test(2);
        return "/WEB-INF/views/main.jsp";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage() {
        return "/WEB-INF/views/main.jsp";
    }


    @GetMapping(value = "/tag-handler")
    public String tagHandler() {
        return "/WEB-INF/views/tag_handler.jsp";
    }

    @PostMapping(value = "/delete-tag")
    public String deleteTag(@ModelAttribute("customTagDTO") CustomTagDTO customTagDTO, Model model) {
        String result = customTagsService.deleteCustomTagById(customTagDTO);
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
    public String addRecipe() {
        return "/WEB-INF/views/recipe_handler.jsp";
    }

    @PostMapping(value = "/recipe-handler")
    public ModelAndView updateRecipe(@RequestParam(name = "recipeID") long recipeID, ModelAndView modelAndView) {
        RecipeDTO recipeDTO = dtoService.findRecipeDTO(recipeID);
        System.out.println("RecipeRandomizerController.updateRecipe recipeDTO: " + recipeDTO);
        modelAndView.addObject("recipeDTOJson", recipeDesignerService.parseRecipeDTOtoJson(recipeDTO));
        modelAndView.setViewName("/WEB-INF/views/recipe_handler.jsp");
        return modelAndView;
    }

    @PostMapping(value = "/recipe-add-or-update")
    public ModelAndView recipeAddOrUpdate(@ModelAttribute("recipeDTO") RecipeDTO recipeDTO, ModelAndView modelAndView,
                                          HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        recipeDTO.setUsername(username);
        recipeDTO = recipeDesignerService.saveRecipeByRecipeDTO(recipeDTO);

//        String message = recipeImageCacheService.updateImageFromCacheWithDelay(recipeDTO);
        ImageBufferDTO imageDTO = new ImageBufferDTO(recipeDTO.getId(), recipeDTO.getName(), recipeDTO.getUsername());
        String message = recipeImageCacheService.updateImageFromBufferWithDelay(imageDTO);

        if (recipeDTO.getId() != null) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                System.out.println("RecipeRandomizerController.recipeAddOrUpdate error when try sleep");
            }
            return getRecipeById(recipeDTO.getId(), modelAndView);
        } else {
            modelAndView.addObject("message", message);
            modelAndView.setViewName("/WEB-INF/views/main.jsp");
            return modelAndView;
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
    public String recipesFound() {
        return "/WEB-INF/views/recipes_found.jsp";
    }

    @GetMapping(value = "/search-recipes-name-like")
    public ModelAndView searchRecipesNameLike(@RequestParam(name = "recipesNameLike", required = false) String recipeName,
                                              ModelAndView modelAndView, HttpServletRequest request) {
        if (recipeName == null || recipeName.isEmpty()) {
            modelAndView.setViewName("/WEB-INF/views/main.jsp");
            return modelAndView;
        }

        String username = request.getUserPrincipal().getName();
        modelAndView.addObject("recipes", recipeService.findRecipesByNameLikeForUser(recipeName, username));
        modelAndView.setViewName("/WEB-INF/views/recipes_found.jsp");
        return modelAndView;
    }

    @GetMapping(value = "/search-recipes-by-form")
    public ModelAndView postSearchRecipesByForm(@ModelAttribute("searchModel") SearchModel searchModel,
                                                ModelAndView modelAndView, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        modelAndView.addObject("recipes", searchFormService.findRecipesForUserBySearchModel(searchModel, username));
        modelAndView.setViewName("/WEB-INF/views/recipes_found.jsp");
        return modelAndView;
    }

    @GetMapping(value = "/recipe-search-randomizer")
    public ModelAndView postRecipeSearchRandomizer(@ModelAttribute("searchModel") SearchModel searchModel,
                                                   ModelAndView modelAndView, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        String recipesIdsJson = searchFormService.findAndRandomizeRecipeIdsForUserJSON(searchModel, username);
        modelAndView.addObject("recipesIdsJson", recipesIdsJson);
        modelAndView.setViewName("/WEB-INF/views/recipe_randomizer.jsp");

        System.out.println("/recipe-search-randomizer  recipesIdsJson.length: " + recipesIdsJson.length());
        return modelAndView;
    }


    @GetMapping(value = "/recipe/{id}")
    public ModelAndView getRecipeById(@PathVariable Long id, ModelAndView modelAndView) {
        RecipeDTO recipeDTO = dtoService.findRecipeDTO(id);
        if (recipeDTO == null) {
            modelAndView.setViewName("/WEB-INF/views/main.jsp");
            return modelAndView;
        }
        modelAndView.addObject("recipeDTO", recipeDTO);
        modelAndView.setViewName("/WEB-INF/views/recipe.jsp");
        return modelAndView;
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
