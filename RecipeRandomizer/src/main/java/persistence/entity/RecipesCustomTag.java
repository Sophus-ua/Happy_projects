package persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "recipes_custom_tags", schema = "recipe_randomizer")
public class RecipesCustomTag {
    @EmbeddedId
    private RecipesCustomTagId id;
    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Recipe recipe;

    @ManyToOne
    @MapsId("customTagId")
    @JoinColumn(name = "custom_tag_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CustomTag customTag;

    @Embeddable
    private static class RecipesCustomTagId implements Serializable {
        @Column(name = "recipe_id", nullable = false)
        private long recipeId;

        @Column(name = "custom_tag_id", nullable = false)
        private long customTagId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecipesCustomTagId that = (RecipesCustomTagId) o;

            if (recipeId != that.recipeId) return false;
            return customTagId == that.customTagId;
        }

        @Override
        public int hashCode() {
            int result = (int) (recipeId ^ (recipeId >>> 32));
            result = 31 * result + (int) (customTagId ^ (customTagId >>> 32));
            return result;
        }
    }
}
