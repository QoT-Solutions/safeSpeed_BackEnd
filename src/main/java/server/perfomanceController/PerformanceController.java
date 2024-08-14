
package server.perfomanceController;
import org.apache.jorphan.collections.HashTree;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
import performance.JMeterFunctions;
import server.Database.DatabaseController;
import server.perfomanceController.paramClasses.TestPlanBody;
import server.perfomanceController.paramClasses.RunTestPlanBody;
import server.perfomanceController.paramClasses.SamplerBody;
import server.perfomanceController.paramClasses.ThreadGroupBody;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    // GET endpoint
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, World!";
    }

    // POST endpoint
    @PostMapping("/createTestPlan")
    public JSONObject createTestPlan(@RequestBody TestPlanBody body) {

        DatabaseController.insertNewTestPlan(body.getUserId(), body.getTitle(), body.getComments());
        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Test plan successfully created");

        return res;
    }

    @PostMapping("/createThreadGroup")
    public JSONObject createThreadGroup(@RequestBody ThreadGroupBody body) {

        DatabaseController.insertNewThreadGroups(body.getUserId(), body.getTestPlanId(), body.getComments(), body.getTitle(), body.getThreads(), body.getRampUp(), body.getHoldTime());
        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Thread group successfully created");

        return res;
    }

    @PostMapping("/createSampler")
    public JSONObject createThreadGroup(@RequestBody SamplerBody body) {

        DatabaseController.insertNewSampler(body.getTestPlanId(), body.getName(), body.getComments(), body.getProtocol(), body.getServer(), body.getType(), body.getPort(), body.getBody());
        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Thread group successfully created");

        return res;
    }

    @PostMapping("/runTestPlan")
    public JSONObject createThreadGroup(@RequestBody RunTestPlanBody body) throws ParseException {
        JSONObject testPlan = DatabaseController.getTestPlan(body.getUserId(), body.getTestPlanId());

        JMeterFunctions jm = new JMeterFunctions();
//        jm.createTestPlan(testPlan.get("title").toString());
//
//        JSONObject threadGroup = (JSONObject) new JSONParser().parse(testPlan.get("threadGroup").toString());
//        String title = threadGroup.get("title").toString();
//        int numThreads = Integer.parseInt(threadGroup.get("threads").toString());
//        int rampUp = Integer.parseInt(threadGroup.get("rampUp").toString());
//        jm.createThreadGroup(title, numThreads, rampUp);

//        HashTree builder = jm.buildTestPlan();
//        jm.runTestPlan(builder);


        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("data",testPlan);

        return res;
    }



}

