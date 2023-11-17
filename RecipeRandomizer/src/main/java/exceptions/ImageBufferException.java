package exceptions;

public class ImageBufferException extends RuntimeException {
    private final String customMessage;

    public ImageBufferException(String customMessage) {
        super();
        this.customMessage = customMessage;
    }
    public ImageBufferException(){
        super();
        this.customMessage = "ImageBufferException";
    }
}
