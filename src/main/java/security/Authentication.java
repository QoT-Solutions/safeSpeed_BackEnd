package security;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Authentication {

    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = "pff8s5ta7eo42jgg411651corg";
    private static final String contextId = "1";
    private static final String contextName = "Default Context";
    private static final String target = "http://localhost:8090/bodgeit";

    private static void setIncludeAndExcludeInContext(ClientApi clientApi) throws UnsupportedEncodingException, ClientApiException {
        String includeInContext = "http://localhost:8090/bodgeit.*";
        String excludeInContext = "http://localhost:8090/bodgeit/logout.jsp";

        clientApi.context.includeInContext(contextName, includeInContext);
        clientApi.context.excludeFromContext(contextName, excludeInContext);
    }


    private static void setLoggedInIndicator(ClientApi clientApi) throws UnsupportedEncodingException, ClientApiException {
        // Prepare values to set, with the logged in indicator as a regex matching the logout link
        String loggedInIndicator = "<a href=\"logout.jsp\">Logout</a>";

        // Actually set the logged in indicator
        clientApi.authentication.setLoggedInIndicator(contextId, java.util.regex.Pattern.quote(loggedInIndicator));

        // Check out the logged in indicator that is set
        System.out.println("Configured logged in indicator regex: "
                + ((ApiResponseElement) clientApi.authentication.getLoggedInIndicator(contextId)).getValue());
    }

    private static void setFormBasedAuthenticationForBodgeit(ClientApi clientApi) throws ClientApiException,
            UnsupportedEncodingException {
        // Setup the authentication method

        String loginUrl = "http://localhost:8090/bodgeit/login.jsp";
        String loginRequestData = "username={%username%}&password={%password%}";

        // Prepare the configuration in a format similar to how URL parameters are formed. This
        // means that any value we add for the configuration values has to be URL encoded.
        StringBuilder formBasedConfig = new StringBuilder();
        formBasedConfig.append("loginUrl=").append(URLEncoder.encode(loginUrl, "UTF-8"));
        formBasedConfig.append("&loginRequestData=").append(URLEncoder.encode(loginRequestData, "UTF-8"));

        System.out.println("Setting form based authentication configuration as: "
                + formBasedConfig.toString());
        clientApi.authentication.setAuthenticationMethod(contextId, "formBasedAuthentication",
                formBasedConfig.toString());

        // Check if everything is set up ok
        System.out
                .println("Authentication config: " + clientApi.authentication.getAuthenticationMethod(contextId).toString(0));
    }

    private static String setUserAuthConfigForBodgeit(ClientApi clientApi) throws ClientApiException, UnsupportedEncodingException {
        // Prepare info
        String user = "Test User";
        String username = "test@example.com";
        String password = "weakPassword";

        // Make sure we have at least one user
        String userId = extractUserId(clientApi.users.newUser(contextId, user));

        // Prepare the configuration in a format similar to how URL parameters are formed. This
        // means that any value we add for the configuration values has to be URL encoded.
        StringBuilder userAuthConfig = new StringBuilder();
        userAuthConfig.append("username=").append(URLEncoder.encode(username, "UTF-8"));
        userAuthConfig.append("&password=").append(URLEncoder.encode(password, "UTF-8"));

        System.out.println("Setting user authentication configuration as: " + userAuthConfig.toString());
        clientApi.users.setAuthenticationCredentials(contextId, userId, userAuthConfig.toString());
        clientApi.users.setUserEnabled(contextId, userId, "true");
        clientApi.forcedUser.setForcedUser(contextId, userId);
        clientApi.forcedUser.setForcedUserModeEnabled(true);

        // Check if everything is set up ok
        System.out.println("Authentication config: " + clientApi.users.getUserById(contextId, userId).toString(0));
        return userId;
    }

    private static String extractUserId(ApiResponse response) {
        return ((ApiResponseElement) response).getValue();
    }

    private static void scanAsUser(ClientApi clientApi, String userId) throws ClientApiException {
        clientApi.spider.scanAsUser(contextId, userId, target, null, "true", null);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws ClientApiException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws ClientApiException, UnsupportedEncodingException {
        ClientApi clientApi = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);

        setIncludeAndExcludeInContext(clientApi);
        setFormBasedAuthenticationForBodgeit(clientApi);
        setLoggedInIndicator(clientApi);
        String userId = setUserAuthConfigForBodgeit(clientApi);
        scanAsUser(clientApi, userId);
    }
}