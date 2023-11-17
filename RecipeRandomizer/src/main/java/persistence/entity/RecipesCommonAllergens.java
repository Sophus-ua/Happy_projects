package persistence.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recipes_common_allergens", schema = "recipe_randomizer")
public class RecipesCommonAllergens {
    @EmbeddedId
    private RecipesCommonAllergensId id;
    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Recipe recipe;

    @ManyToOne
    @MapsId("allergenId")
    @JoinColumn(name = "allergen_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Allergen allergen;

    @Embeddable
    private static class RecipesCommonAllergensId implements Serializable {
        @Column(name = "recipe_id", nullable = false)
        private long recipeId;

        @Column(name = "allergen_id", nullable = false)
        private long allergenId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecipesCommonAllergensId that = (RecipesCommonAllergensId) o;

            if (recipeId != that.recipeId) return false;
            return allergenId == that.allergenId;
        }

        @Override
        public int hashCode() {
            int result = (int) (recipeId ^ (recipeId >>> 32));
            result = 31 * result + (int) (allergenId ^ (allergenId >>> 32));
            return result;
        }
    }
}
