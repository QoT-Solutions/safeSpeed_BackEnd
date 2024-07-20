package security;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;

import java.util.List;

public class SpiderCrawler {

    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8080;
    // Change to match the API key set in ZAP, or use NULL if the API key is disabled
    private static final String ZAP_API_KEY = "pff8s5ta7eo42jgg411651corg";
    // The URL of the application to be tested
    private static final String TARGET = "https://demo.guru99.com/test/newtours/";

    public static ClientApi spiderCrawl() {
        ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);

        try {
            // Start spidering the target
            System.out.println("Spidering target : " + TARGET);
            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
            String scanID;
            int progress;

            // The scan returns a scan id to support concurrent scanning
            scanID = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanID)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider completed");
            // If required post process the spider results
            List<ApiResponse> spiderResults = ((ApiResponseList) api.spider.results(scanID)).getItems();
            System.out.println(spiderResults);

            // TODO: Explore the Application more with Ajax Spider or Start scanning the application for vulnerabilities

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }

        return api;
    }
}