package persistence.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "image_buffer", schema = "recipe_randomizer",
        uniqueConstraints = @UniqueConstraint(columnNames = "image_key"))
public class ImageBuffer {
    @Id
    @Column(name = "image_key", nullable = false, unique = true)
    private String imageKey;

    @Column(name = "image_data", columnDefinition = "mediumblob", nullable = false)
    private byte[] imageData;

    @Override
    public String toString(){
        return "ImageBuffer: " + imageKey;
    }
}
