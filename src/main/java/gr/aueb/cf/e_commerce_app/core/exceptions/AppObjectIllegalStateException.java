package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectIllegalStateException extends AppGenericException {
    private static final String DEFAULT_CODE = "IllegalState";
    public AppObjectIllegalStateException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
