package persistence.dao.services.implementations;


import exceptions.ImageBufferException;
import lombok.extern.slf4j.Slf4j;
import models.ImageBufferDTO;
import models.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.repositories.IImageBufferRepository;
import persistence.dao.repositories.IRecipeRepository;
import persistence.dao.repositories.IUserRepository;
import persistence.dao.services.interfaces.IRecipeImageCacheService;
import persistence.entity.ImageBuffer;
import persistence.entity.User;

import java.util.Base64;
import java.util.Optional;


@Slf4j
@Service
public class RecipeImageCacheServiceImpl implements IRecipeImageCacheService {
    private final CacheManager cacheManager;
    private final IRecipeRepository recipeRepository;
    private final IImageBufferRepository imageBufferRepository;
    private final IUserRepository userRepository;

    @Autowired
    public RecipeImageCacheServiceImpl(IRecipeRepository recipeRepository, IUserRepository userRepository,
                                       IImageBufferRepository imageBufferRepository, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.recipeRepository = recipeRepository;
        this.imageBufferRepository = imageBufferRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void saveImageToBufferWithEvictionDelay(ImageBufferDTO imageDTO) throws ImageBufferException {
        if (imageDTO == null ||
                imageDTO.getImageKey() == null || imageDTO.getImageKey().isEmpty() ||
                imageDTO.getUsername() == null || imageDTO.getUsername().isEmpty() ||
                imageDTO.getBase64Image() == null || imageDTO.getBase64Image().isEmpty())
            throw new ImageBufferException();
        System.out.println("RecipeImageCacheServiceImpl.saveImageToBufferWithEvictionDelay imageDTO is Good");

        byte[] imageBytes = getMediumblobImage(imageDTO);
        if (imageBytes == null || imageBytes.length == 0)
            throw new ImageBufferException();
        System.out.println("RecipeImageCacheServiceImpl.saveImageToBufferWithEvictionDelay imageBytes are Good");

        Optional<User> optional = userRepository.findByUsername(imageDTO.getUsername());
        if (!optional.isPresent())
            throw new ImageBufferException();
        System.out.println("RecipeImageCacheServiceImpl.saveImageToBufferWithEvictionDelay Optional<User> isPresent()");

        ImageBuffer imageBuffer = new ImageBuffer(imageDTO.getImageKey(), optional.get(), imageBytes);
        System.out.println("RecipeImageCacheServiceImpl.saveImageToBufferWithEvictionDelay imageBuffer: " + imageBuffer.getId());

        imageBuffer = imageBufferRepository.save(imageBuffer);
        deleteImageFromBufferWithDelay(imageBuffer);
    }

    private void deleteImageFromBufferWithDelay(ImageBuffer imageBuffer) {
        Thread deleteThread = new Thread(() -> {
            try {
                Thread.sleep(30 * 1000); // Затримка 0.5 хвилини
            } catch (InterruptedException e) {
                System.err.println("Error in RecipeImageCacheServiceImpl.deleteImageFromCache when thread try to sleep");
            }
            if (imageBufferRepository.existsById(imageBuffer.getId()))
                imageBufferRepository.delete(imageBuffer);
        });
        deleteThread.setName("threadDeleteFromImageBuffer");
        deleteThread.start();
    }

    @Override
    public String updateImageFromBufferWithDelay(ImageBufferDTO imageDTO) {
        if (imageDTO == null)
            return Messages.FAILED_TO_SAVE.message;

        Thread updateImageThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    System.err.println("Error in RecipeImageCacheServiceImpl.updateImageWithDelay when thread try to sleep");
                }
                Integer userId = userRepository.getIdByUsername(imageDTO.getUsername());
                ImageBuffer.ImageBufferId imageBufferId =
                        new ImageBuffer.ImageBufferId(imageDTO.getImageKey(), userId.longValue());

                Optional<ImageBuffer> optional = imageBufferRepository.findById(imageBufferId);
                byte[] imageData = null;
                if (optional.isPresent())
                    imageData = optional.get().getImageData();

                if (imageData != null && imageData.length > 0) {
                    recipeRepository.updateImageDataById(imageData, imageDTO.getRecipeId());
                    imageBufferRepository.delete(optional.get());
                    break;
                }
            }
        });
        updateImageThread.setName("ThreadUpdateFromImageBuffer");
        updateImageThread.start();

        return Messages.SUCCESSFULLY_SAVED.message;
    }

    private enum Messages {
        SUCCESSFULLY_SAVED("Рецепт був успішно збережений "),
        FAILED_TO_SAVE("Не вдалось зберегти рецепт :( ");

        final String message;

        Messages(String message) {
            this.message = message;
        }
    }

    @Nullable
    private byte[] getMediumblobImage(ImageBufferDTO imageDTO) {
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

    @Override
    @Nullable
    @Cacheable(value = "recipeImage", condition = "#result != null", key = "#imageDTO.getImageKey()")
    public byte[] saveImageToCacheWithEvictionDelay(ImageBufferDTO imageDTO) {
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
}
