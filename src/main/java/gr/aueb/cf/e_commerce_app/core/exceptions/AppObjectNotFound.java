package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectNotFound extends AppGenericException{
    private static final String DEFAULT_CODE = "NotFound";

    public AppObjectNotFound(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
