package security;

import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PassiveScan {

    private static final int ZAP_PORT = 8080;
    private static final String ZAP_API_KEY = "pff8s5ta7eo42jgg411651corg";
    private static final String ZAP_ADDRESS = "localhost";

    public static void main(String[] args) {
        ClientApi api = SpiderCrawler.spiderCrawl();
        int numberOfRecords;

        try {
            // TODO : explore the app (Spider, etc) before using the Passive Scan API, Refer the explore section for details
            // Loop until the passive scan has finished
            while (true) {
                Thread.sleep(2000);
                api.pscan.recordsToScan();
                numberOfRecords = Integer.parseInt(((ApiResponseElement) api.pscan.recordsToScan()).getValue());
                System.out.println("Number of records left for scanning : " + numberOfRecords);
                if (numberOfRecords == 0) {
                    break;
                }
            }
            System.out.println("Passive Scan completed");

            // Print Passive scan results/alerts
            System.out.println("Alerts:");

            System.out.println(new String(api.core.xmlreport(), StandardCharsets.UTF_8));
            try {
                FileWriter writer = new FileWriter("report.html");

                // Write HTML content to the file
                System.out.println(new String(api.core.jsonreport(), StandardCharsets.UTF_8));
                writer.write(new String(api.core.htmlreport(), StandardCharsets.UTF_8));


                writer.close();
                System.out.println("Successfully wrote HTML content to ");
            } catch (IOException e) {
                System.err.println("Error writing to file " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }
}