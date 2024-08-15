//
//package server.securityController;
//import com.example.SafeSpeed.SafeSpeedController;
//import netscape.javascript.JSObject;
//import org.json.simple.JSONObject;
//import org.springframework.core.annotation.*;
//import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
//import org.springframework.stereotype.*;      // Importing the necessary annotations
//import security.PassiveScan;
//import server.Database.DatabaseController;
//import server.securityController.ParamClasses.PassiveScanBody;
//
//@RestController
//@RequestMapping("/security")
//public class SecurityController
//{
//
//    // GET endpoint
//    @GetMapping("/getPSNReport")
//    public String getPSNReport() {
//        return "Hello, World!";
//    }
//
//    @GetMapping("/getJSONPSNReport")
//    public JSONObject getJSONPSNReport() {
//        //get report
//        String report = DatabaseController.getPSNJSONReports();
//        //Define json object
//        JSONObject res = new JSONObject();
//        res.put("Status", 200);
//        res.put("Report", report);
//        return res;
//    }
//
//    @GetMapping("/getHTLMPSNReport")
//    public JSONObject getHTLMPSNReport() {
//        //get report
//        String report = DatabaseController.getPSNHTMLReports();
//        //Define json object
//        JSONObject res = new JSONObject();
//        res.put("Status", 200);
//        res.put("Report", report);
//        return res;
//    }
//
//    // POST endpoint
//    @PostMapping("/runPassiveScan")
//    public String postGreeting(@RequestBody PassiveScanBody body) {
//
//        // Call the SafeSpeedController class
//        PassiveScan passiveScan = new PassiveScan();
//        passiveScan.triggerPassiveScan(body.getBaseUrl());
//
//        return "{'Status': 'Staterted passive scan',\n'Code': 200}";
//    }
//
//
//}
//
