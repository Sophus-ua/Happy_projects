package exceptions;

import lombok.Getter;

@Getter
public class EmptyRecipeIdsException extends RuntimeException {
    private final String customMessage;

    public EmptyRecipeIdsException(String customMessage) {
        super();
        this.customMessage = customMessage;
    }
    public EmptyRecipeIdsException(){
        super();
        this.customMessage = "Список ID рецептів порожній";
    }
}
