package controllers;

import exceptions.DatabaseUpdateException;
import jakarta.servlet.http.HttpServletRequest;
import models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import persistence.dao.services.interfaces.IDTOService;
import persistence.dao.services.interfaces.IRecipeImageCacheService;
import persistence.dao.services.interfaces.IRecipeService;
import org.springframework.http.HttpHeaders;


import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated")
@RequestMapping("/data")
public class RecipeDataRestController {

    private IDTOService dtoService;
    private IRecipeService recipeService;
    private IRecipeImageCacheService recipeImageCacheService;

    @PostMapping(value = "/add-recipe-to-mine")
    public void addRecipeToMine(@RequestBody Map<String, Object> request, Authentication authentication) {
        try {
            Long commonRecipeId = Long.valueOf(request.get("recipeId").toString());
            String username = authentication.getName();
            System.out.println("commonRecipeId: " + commonRecipeId +" username: "+ username);
            recipeService.copyRecipeToUserById(username, commonRecipeId);
        } catch (DatabaseUpdateException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostMapping(value = "/has-this-common-recipe", produces = "application/json")
    public ResponseEntity<Boolean> commonRecipeIsPresent(@RequestBody Map<String, Object> request,
                                                         Authentication authentication) {
        try {
            Long commonRecipeId = Long.valueOf(request.get("recipeId").toString());
            String username = authentication.getName();
            boolean  commonRecipeIsPresent = recipeService.commonRecipeIsPresent(commonRecipeId, username);
            return new ResponseEntity<>(commonRecipeIsPresent, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    @GetMapping(value = "/all-meal-categories", produces = "application/json")
    public ResponseEntity<List<MealCategoryDTO>> getAllMealCategorises() {
        try {
            List<MealCategoryDTO> mealCategories = dtoService.findAllMealCategories();
            return new ResponseEntity<>(mealCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all-regional-cuisines", produces = "application/json")
    public ResponseEntity<List<RegionalCuisineDTO>> getAllRegionalCuisines() {
        try {
            List<RegionalCuisineDTO> regionalCuisines = dtoService.findAllRegionalCuisines();
            return new ResponseEntity<>(regionalCuisines, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all-dishes-by-ingredients", produces = "application/json")
    public ResponseEntity<List<DishByIngredientsDTO>> getAllDishesByIngredients() {
        try {
            List<DishByIngredientsDTO> dishesByIngredients = dtoService.findAllDishesByIngredients();
            return new ResponseEntity<>(dishesByIngredients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all-allergens", produces = "application/json")
    public ResponseEntity<List<AllergenDTO>> getAllAllergens() {
        try {
            List<AllergenDTO> allergens = dtoService.findAllAllergens();
            return new ResponseEntity<>(allergens, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/all-custom-tags", produces = "application/json")
    public ResponseEntity<List<CustomTagDTO>> getAllCustomTags(HttpServletRequest request) {
        try {
            String username = request.getUserPrincipal().getName();
            List<CustomTagDTO> customTags = dtoService.findAllCustomTagsForUser(username);
            return new ResponseEntity<>(customTags, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/recipe-image-data/{recipeId}")
    public ResponseEntity<byte[]> getRecipeImageData(@PathVariable Long recipeId) {
        try {
            byte[] imageData = recipeService.getImageDataById(recipeId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/recipe-name/{recipeId}", produces = "application/json")
    public ResponseEntity<String> getRecipeName(@PathVariable Long recipeId) {
        try {
            String recipeName = recipeService.findNameById(recipeId);
            System.out.println("RecipeDataRestController.getRecipeName: " + recipeName);
            return new ResponseEntity<>(recipeName, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/imageUpload")
    public ResponseEntity<String> imageUpload(@RequestBody ImageBufferDTO imageDTO,
                                              HttpServletRequest request) {
        try {
//            byte[] imageByte = recipeImageCacheService.saveImageToCacheWithEvictionDelay(imageDTO);
//            if (imageByte == null || imageByte.length ==0){
//                System.out.println("RecipeDataRestController.imageUpload imageByte is null or empty");
//            } else {
//                System.out.println("RecipeDataRestController.imageUpload imageByte is good. CashKey: " + imageDTO.getImageKey());
//                imageByte = null;
//            }
            imageDTO.setUsername(request.getUserPrincipal().getName());

            System.out.println("RecipeDataRestController.imageUpload imageDTO.getImageKey(): " + imageDTO.getImageKey());
            System.out.println("RecipeDataRestController.imageUpload imageDTO.getUserId(): " + imageDTO.getUsername());

            recipeImageCacheService.saveImageToBufferWithEvictionDelay(imageDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Autowired
    public void setRecipeService(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setRegionalCuisineService(IDTOService dtoService) {
        this.dtoService = dtoService;
    }

    @Autowired
    public void setRecipeImageCacheService(IRecipeImageCacheService recipeImageCacheService) {
        this.recipeImageCacheService = recipeImageCacheService;
    }
}
