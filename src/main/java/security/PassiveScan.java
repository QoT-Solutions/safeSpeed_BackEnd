package security;

import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import server.Database.DatabaseController;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PassiveScan {



    public void triggerPassiveScan(String target) {
        ClientApi api = SpiderCrawler.spiderCrawl(target);
        int numberOfRecords;

        try {
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

                String jsonReport = new String(api.core.jsonreport(), StandardCharsets.UTF_8);
                String htmlReport = new String(api.core.htmlreport(), StandardCharsets.UTF_8);

                // Write HTML content to the file
                System.out.println("Writing HTML content to report.html");
                writer.write(htmlReport);
                writer.close();

                //write to DB
                DatabaseController.insertPSNReports("Passive Scan Report", jsonReport, htmlReport);
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