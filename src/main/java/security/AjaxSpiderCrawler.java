package security;

import org.zaproxy.clientapi.core.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AjaxSpiderCrawler {

    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = "pff8s5ta7eo42jgg411651corg";
    private static final String ZAP_ADDRESS = "localhost";
    private static final String TARGET = "https://demo.guru99.com";

    public static void main(String[] args) throws ClientApiException {
        // Create the ZAP Client
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);

        try {
            // Start spidering the target
            System.out.println("Ajax Spider target : " + TARGET);
            ApiResponse resp = api.ajaxSpider.scan(TARGET, null, null, null);
            String status;

            long startTime = System.currentTimeMillis();
            long timeout = TimeUnit.MINUTES.toMillis(2); // Two minutes in milli seconds
            // Loop until the ajax spider has finished or the timeout has exceeded
            while (true) {
                Thread.sleep(2000);
                status = (((ApiResponseElement) api.ajaxSpider.status()).getValue());
                System.out.println("Spider status : " + status);
                if ((System.currentTimeMillis() - startTime) < timeout) {
                    break;
                }
            }
            System.out.println("Ajax Spider completed");
            // Perform additional operations with the Ajax Spider results
            List<ApiResponse> ajaxSpiderResponse = ((ApiResponseList) api.ajaxSpider.results("0", "10")).getItems();
            System.out.println(ajaxSpiderResponse);
            // TODO: Start scanning(passive/active scan) the application to find vulnerabilities

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            api.ajaxSpider.stop();
            e.printStackTrace();
        }
    }
}