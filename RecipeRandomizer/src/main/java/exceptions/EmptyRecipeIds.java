package exceptions;

import lombok.Getter;

@Getter
public class EmptyRecipeIds extends RuntimeException {
    private final String customMessage;

    public EmptyRecipeIds(String message, String customMessage) {
        super(message);
        this.customMessage = customMessage;
    }
    public EmptyRecipeIds(){
        super();
        this.customMessage = "recipeIds is empty";
    }

}
