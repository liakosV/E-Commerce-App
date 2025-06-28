package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectNotAuthorized extends AppGenericException {
    private static final String DEFAULT_CODE = "NotAuthorized";
    public AppObjectNotAuthorized(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
