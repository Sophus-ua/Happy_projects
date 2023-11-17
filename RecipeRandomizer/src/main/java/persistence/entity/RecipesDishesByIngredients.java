package persistence.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recipes_dishes_by_ingredients", schema = "recipe_randomizer")
public class RecipesDishesByIngredients {

    @EmbeddedId
    private RecipesDishesByIngredientsId id;
    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Recipe recipe;

    @ManyToOne
    @MapsId("dishByIngredientsId")
    @JoinColumn(name = "dish_by_ingredients_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DishByIngredients dishByIngredients;

    @Embeddable
    private static class RecipesDishesByIngredientsId implements Serializable {
        @Column(name = "recipe_id", nullable = false)
        private long recipeId;

        @Column(name = "dish_by_ingredients_id", nullable = false)
        private long dishByIngredientsId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RecipesDishesByIngredientsId that = (RecipesDishesByIngredientsId) o;

            if (recipeId != that.recipeId) return false;
            return dishByIngredientsId == that.dishByIngredientsId;
        }

        @Override
        public int hashCode() {
            int result = (int) (recipeId ^ (recipeId >>> 32));
            result = 31 * result + (int) (dishByIngredientsId ^ (dishByIngredientsId >>> 32));
            return result;
        }
    }
}
