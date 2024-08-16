package performance;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.Database.DB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportProcessor {

    static JSONParser parser = new JSONParser();

//    public  static void main(String[] args) {
//        List<Map<String, String>> records = readJTLFile("./example.jtl");
//        try {
//            JSONArray jsonArray = convertToJson(records);
//            System.out.println(jsonArray);
//            DB db = new DB();
//            db.storeReport(jsonArray, "55");
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//    }

    public static List<Map<String, String>> readJTLFile(String filePath) {
        List<Map<String, String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Read header line
            String[] headers = br.readLine().split(",");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    record.put(headers[i], values[i]);
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static JSONArray convertToJson(List<Map<String, String>> records) throws IOException, ParseException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for (Map<String, String> record : records) {
            for (Map.Entry<String, String> entry : record.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue()); // Add key-value pairs to JSONObject
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //Read JSON file and return JSONObject
    public static JSONArray readJsonFile(String filePath) throws IOException, ParseException {
        Reader reader = new FileReader(filePath);
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        jsonArray.add(jsonObject);
        return jsonArray;
    }
}
