package gr.aueb.cf.e_commerce_app.core.exceptions;

public class AppObjectAccessDeniedException extends AppGenericException {
  private final static String DEFAULT_CODE = "AccessDenied";
    public AppObjectAccessDeniedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
