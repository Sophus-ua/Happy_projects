package models;

import lombok.Data;
import persistence.entity.CustomTag;


@Data
public class CustomTagDTO {
    private long id;
    private String name;
    private String username;

    public CustomTagDTO(){}
    public CustomTagDTO(CustomTag customTag) {
        this.id = customTag.getId();
        this.name = customTag.getName();
    }
}
