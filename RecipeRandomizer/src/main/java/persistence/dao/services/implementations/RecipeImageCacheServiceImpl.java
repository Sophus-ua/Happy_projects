package persistence.dao.services.implementations;


import lombok.extern.slf4j.Slf4j;
import models.ImageDTO;
import models.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import persistence.dao.repositories.IImageBufferRepository;
import persistence.dao.repositories.IRecipeRepository;
import persistence.dao.services.interfaces.IRecipeImageCacheService;
import persistence.entity.ImageBuffer;

import java.util.Base64;

import java.util.Optional;


@Slf4j
@Service
public class RecipeImageCacheServiceImpl implements IRecipeImageCacheService {
    private CacheManager cacheManager;
    private IRecipeRepository recipeRepository;
    private IImageBufferRepository imageBufferRepository;



    @Override
    @Nullable
    @Cacheable(value = "recipeImage", condition = "#result != null", key = "#imageDTO.getImageKey()")
    public byte[] saveImageToCacheWithEvictionDelay(ImageDTO imageDTO) {
        if (imageDTO == null || imageDTO.getBase64Image() == null || imageDTO.getBase64Image().isEmpty() ||
                imageDTO.getImageKey() == null || imageDTO.getImageKey().isEmpty()) {
            System.out.println("RecipeImageCacheServiceImpl.saveImageToCacheWithEvictionDelay imageDTO == null or empty");
            return null;
        }
        byte[] imageBytes = getMediumblobImage(imageDTO);
        if (imageBytes == null || imageBytes.length == 0) {
            System.out.println("RecipeImageCacheServiceImpl.saveImageToCacheWithEvictionDelay imageBytes == null or empty");
            return null;
        }
        evictFromCacheWithDelay(imageDTO.getImageKey());

        return imageBytes;
    }

    private void evictFromCacheWithDelay(String cacheKey) {
        Thread deleteThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3 * 1000); // Затримка 0.5 хвилини
                } catch (InterruptedException e) {
                    System.err.println("Error in RecipeImageCacheServiceImpl.deleteImageFromCache when thread try to sleep");
                }
                Cache cache = cacheManager.getCache(CacheName.recipeImage.name());
                if (cache != null) {
                    Cache.ValueWrapper valueWrapper = cache.get(cacheKey);
                    if (valueWrapper != null && valueWrapper.get() != null) {
                        System.out.println("RecipeImageCacheServiceImpl.evictFromCacheWithDelay cache is good !!! CashKey: " + cacheKey);
                        if (i == 9)
                            cache.evict(cacheKey);
                    } else {
                        System.out.println("RecipeImageCacheServiceImpl.evictFromCacheWithDelay cache is empty. CashKey: " + cacheKey);
                    }
                }
            }
        });
        deleteThread.setName("deleteThreadFromCache");
        deleteThread.start();
    }


    @Override
    public String updateImageFromCacheWithDelay(RecipeDTO recipeDTO) {
        if (recipeDTO == null)
            return "Не вдалось зберегти рецепт";

        Thread updateImageThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    System.err.println("Error in RecipeImageCacheServiceImpl.updateImageFromCacheWithDelay when thread try to sleep");
                }
                Cache cache = cacheManager.getCache(CacheName.recipeImage.name());
                if (cache != null) {
                    Cache.ValueWrapper valueWrapper = cache.get(recipeDTO.getName());

                    if (valueWrapper != null) {
                        System.out.println("RecipeImageCacheServiceImpl.updateImageFromCacheWithDelay valueWrapper is good !!!");
                        byte[] imageData = (byte[]) valueWrapper.get();
                        recipeRepository.updateImageDataById(imageData, recipeDTO.getId());
                        cache.evict(recipeDTO.getName());
                        break;
                    } else {
                        System.out.println("RecipeImageCacheServiceImpl.updateImageFromCacheWithDelay valueWrapper is null. CashKey: " + recipeDTO.getName());
                    }
                }
            }
        });
        updateImageThread.setName("updateImageThread");
        updateImageThread.start();

        return "Рецепт збережено успішно";
    }

    private enum CacheName {
        recipeImage
    }


    @Override
    public void saveImageToBufferWithEvictionDelay(ImageDTO imageDTO) {
        if (imageDTO != null && imageDTO.getBase64Image() != null && !imageDTO.getBase64Image().isEmpty() &&
                imageDTO.getImageKey() != null && !imageDTO.getImageKey().isEmpty()) {

            byte[] imageBytes = getMediumblobImage(imageDTO);
            if (imageBytes != null && imageBytes.length > 0) {
                ImageBuffer imageBuffer = new ImageBuffer();
                imageBuffer.setImageData(imageBytes);
                imageBuffer.setImageKey(imageDTO.getImageKey());

                imageBuffer = imageBufferRepository.save(imageBuffer);
                deleteImageFromBufferWithDelay(imageBuffer);
            }
        }
    }

    private void deleteImageFromBufferWithDelay(ImageBuffer imageBuffer) {
        Thread deleteThread = new Thread(() -> {
            try {
                Thread.sleep(30 * 1000); // Затримка 0.5 хвилини
            } catch (InterruptedException e) {
                System.err.println("Error in RecipeImageCacheServiceImpl.deleteImageFromCache when thread try to sleep");
            }
            if (imageBufferRepository.existsById(imageBuffer.getImageKey()))
                imageBufferRepository.delete(imageBuffer);
        });
        deleteThread.setName("deleteThreadFromBuffer");
        deleteThread.start();
    }

    @Override
    public String updateImageFromBufferWithDelay(RecipeDTO recipeDTO) {
        if (recipeDTO == null)
            return "Не вдалось зберегти рецепт";

        Thread updateImageThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.err.println("Error in RecipeImageCacheServiceImpl.updateImageWithDelay when thread try to sleep");
                }
                Optional<ImageBuffer> optional = imageBufferRepository.findById(recipeDTO.getName());
                byte[] imageData = null;
                if (optional.isPresent())
                    imageData = optional.get().getImageData();

                if (imageData != null && imageData.length > 0) {
                    recipeRepository.updateImageDataById(imageData, recipeDTO.getId());
                    imageBufferRepository.delete(optional.get());
                    break;
                }
            }
        });
        updateImageThread.setName("updateImageThread");
        updateImageThread.start();

        return "Рецепт збережено успішно";
    }


    @Nullable
    private byte[] getMediumblobImage(ImageDTO imageDTO) {
        byte[] imageData = Base64.getDecoder().decode(imageDTO.getBase64Image());
//        try {
//            ImageInfo imageInfo = Imaging.getImageInfo(imageData);
//
//            String format = imageInfo.getFormatName().toLowerCase();
//            List<String> allowedFormats = Arrays.asList("jpg", "jpeg", "png", "bmp");
//            if (!allowedFormats.contains(format)) return null;
//
//            float imageSize = imageInfo.getPhysicalHeightInch() * imageInfo.getPhysicalWidthInch();
//            float maxSize = 16 * 1024 * 1024; // 16 MB
//            if (imageSize > maxSize) return null;
//
//        } catch (ImageReadException | IOException e) {
//            return null;
//        }
        return imageData;
    }

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Autowired
    public void setRecipeRepository(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Autowired
    public void setImageBufferRepository(IImageBufferRepository imageBufferRepository) {
        this.imageBufferRepository = imageBufferRepository;
    }

}
