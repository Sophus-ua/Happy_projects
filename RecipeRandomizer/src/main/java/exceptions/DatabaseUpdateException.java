package exceptions;

public class DatabaseUpdateException extends RuntimeException {
    private final String customMessage;

    public DatabaseUpdateException(String customMessage) {
        super();
        this.customMessage = customMessage;
    }
    public DatabaseUpdateException(){
        super();
        this.customMessage = "Помилка оновлення бази данних";
    }
}
