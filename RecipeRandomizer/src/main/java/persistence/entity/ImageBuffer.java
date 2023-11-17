package persistence.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "image_buffer", schema = "recipe_randomizer",
        uniqueConstraints = @UniqueConstraint(columnNames = {"image_key", "user_id"}))
public class ImageBuffer {

    @EmbeddedId
    private ImageBufferId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "image_data", columnDefinition = "mediumblob", nullable = false)
    private byte[] imageData;

    public ImageBuffer() {}

    public ImageBuffer(String imageKey, User user, byte[] imageData) {
        this.id = new ImageBufferId(imageKey, user.getId());
        user.getImageBuffers().add(this);
        this.user = user;
        this.imageData = imageData;
    }

    @Data
    @Embeddable
    public static class ImageBufferId implements Serializable {
        @Column(name = "image_key", nullable = false)
        private String imageKey;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        public ImageBufferId() {
        }

        public ImageBufferId(String imageKey, Long userId) {
            this.imageKey = imageKey;
            this.userId = userId;
        }
    }

    @Override
    public String toString() {
        return "ImageBuffer: user " + id.userId + ", key " + id.imageKey;
    }
}
