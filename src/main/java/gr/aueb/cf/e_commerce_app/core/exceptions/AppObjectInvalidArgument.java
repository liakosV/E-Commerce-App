package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectInvalidArgument extends AppGenericException {
    private static final String DEFAULT_CODE = "InvalidArgument";
    public AppObjectInvalidArgument(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
