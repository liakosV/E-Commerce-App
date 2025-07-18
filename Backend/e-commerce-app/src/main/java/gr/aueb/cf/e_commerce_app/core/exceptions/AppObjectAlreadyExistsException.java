package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectAlreadyExistsException extends AppGenericException {
    private final static String DEFAULT_CODE = "AlreadyExists";
    public AppObjectAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
