package security;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;
import server.Database.DB;

import java.nio.charset.StandardCharsets;

public class PassiveScan {

    String jsonReport;

    public void  triggerPassiveScan(String target) {
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

            jsonReport = new String(api.core.jsonreport(), StandardCharsets.UTF_8);
//            System.out.println(jsonReport);

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveResults(String testPlanId){
        try{
            JSONParser parser = new JSONParser();
            JSONArray reports = new JSONArray();
            DB db = new DB();
            JSONObject data = (JSONObject) parser.parse(jsonReport);
            JSONArray sites = (JSONArray) data.get("site");

            for (Object site : sites) {
                JSONObject siteObj = (JSONObject) site;
                JSONArray alerts = (JSONArray) siteObj.get("alerts");
                for (Object alert : alerts) {
                    JSONObject alertObj = (JSONObject) alert;
                    alertObj.put("testPlanId", testPlanId);
                    alertObj.put("site", siteObj.get("name"));
                    alertObj.put("port", siteObj.get("port"));
                    reports.add(alertObj);
                    System.out.println(alertObj);
                }
            }

            db.storeReport(reports, testPlanId, "securityReports");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}