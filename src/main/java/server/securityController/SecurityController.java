
package server.securityController;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
import security.PassiveScan;
import server.Database.DB;
import server.securityController.ParamClasses.PassiveScanBody;

@RestController
@RequestMapping("/security")
public class SecurityController
{

    @GetMapping("/getSecurityReport")
    public JSONArray getJSONPSNReport(@RequestParam String testPlanId) {
        DB db = new DB();
        //Define json object
        return db.getReports(testPlanId, "securityReports");
    }

    // POST endpoint
    @PostMapping("/runPassiveScan")
    public String postGreeting(@RequestBody PassiveScanBody body) {

        // Call the SafeSpeedController class
        PassiveScan passiveScan = new PassiveScan();
        System.out.println("Starting passive scan");
        System.out.println("Base URL: " + body.getBaseUrl());
        passiveScan.triggerPassiveScan(body.getBaseUrl());
        passiveScan.saveResults(body.getTestPlanId());

        return "{'Status': 'Staterted passive scan',\n'Code': 200}";
    }


}

