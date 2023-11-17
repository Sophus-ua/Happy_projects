package models;

import lombok.Data;

@Data
public class ImageBufferDTO {
    private Long recipeId;
    private String imageKey;
    private String username;
    private String base64Image;

    public ImageBufferDTO (){}
    public ImageBufferDTO (Long recipeId, String imageKey, String username){
        this.recipeId =recipeId;
        this.imageKey = imageKey;
        this.username = username;
    }
}
