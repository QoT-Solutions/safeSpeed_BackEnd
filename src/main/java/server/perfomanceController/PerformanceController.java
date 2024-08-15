
package server.perfomanceController;
import jakarta.annotation.PostConstruct;
import org.apache.jmeter.report.config.ConfigurationException;
import org.apache.jmeter.report.dashboard.GenerationException;
import org.apache.jorphan.collections.HashTree;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;  // Importing the necessary annotations
import performance.JMeterFunctions;
import server.Database.DB;
import server.Database.DatabaseController;
import server.perfomanceController.paramClasses.TestPlanBody;
import server.perfomanceController.paramClasses.RunTestPlanBody;
import server.perfomanceController.paramClasses.SamplerBody;
import server.perfomanceController.paramClasses.ThreadGroupBody;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    DB db;
    private static Logger logger;

    @PostConstruct
    public void initialise() {
        logger = LoggerFactory.getLogger(PerformanceController.class);
        db = new DB();
    }

    // GET endpoint
    @GetMapping("/hello")
    public String getHello() {
        return "Hello, World!";
    }

    // POST endpoint
    @PostMapping("/createTestPlan")
    public JSONObject createTestPlan(@RequestBody TestPlanBody body) {
        logger.info("Creating test plan");
        String response = db.createTestPlan(body.getUserId(), body.getTitle(), body.getDescription(), body.getType());

        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Test plan successfully created");
        res.put("TestPlanId", response);

        return res;
    }

    @PostMapping("/createThreadGroup")
    public JSONObject createThreadGroup(@RequestBody ThreadGroupBody body) {
        db.createThreadGroup(body.getTestPlanId(), body.getName(), body.getComments(), body.getThreads(), body.getRampUp(), body.getHoldTime());

        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Thread group successfully created");

        return res;
    }

    @PostMapping("/createSampler")
    public JSONObject createThreadGroup(@RequestBody SamplerBody body) {
        db.createSampler(body.getTestPlanId(), body.getName(), body.getComments(), body.getDomain(), body.getPath(), body.getMethod());

        JSONObject res = new JSONObject();
        res.put("Status", 200);
        res.put("Message", "Thread group successfully created");

        return res;
    }

    @PostMapping("/runTestPlan")
    public CompletableFuture<org.json.simple.JSONObject> createThreadGroup(@RequestBody RunTestPlanBody body) throws ParseException {
        return CompletableFuture.supplyAsync(() -> {
            JMeterFunctions jmeter = new JMeterFunctions();

            JSONObject testPlan = db.getTestPlan(body.getTestPlanId());
            JSONObject threadGroup = db.getThreadGroup(body.getTestPlanId());
            List<JSONObject> samplers = db.getSamplers(body.getTestPlanId());

            System.out.println("Test plan: "+testPlan);
            System.out.println("Thread group: "+threadGroup);
            System.out.println("Samplers: "+samplers);
            try {
                jmeter.createTestPlan(testPlan);
                jmeter.createThreadGroup(threadGroup);
                jmeter.createMultipleSamplers(samplers);
                jmeter.addSummaryReporter();
                jmeter.runTestPlan();
                jmeter.generateReportAfterRun(body.getTestPlanId());
            } catch (ConfigurationException e) {
                Thread.currentThread().interrupt();
            } catch (GenerationException e) {
                throw new RuntimeException(e);
            }

            JSONObject res = new JSONObject();
            res.put("Status", 200);
            res.put("Message", "Test started");
            return res;
        });

    }



}

