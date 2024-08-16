package performance;


import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.CSVDataSet;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.gui.CookiePanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.report.config.ConfigurationException;
import org.apache.jmeter.report.dashboard.GenerationException;
import org.apache.jmeter.report.dashboard.ReportGenerator;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jmeter.timers.gui.ConstantTimerGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import server.Database.DB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

public class JMeterFunctions {

        private StandardJMeterEngine jmeter;
        private ListedHashTree testPlanHarshTree;
        private TestPlan testPlan;
        private  HashTree threadGroupHashTree;
        ReportGenerator reportGenerator;
        File logFile;

        public JMeterFunctions() {
            // Set jmeter home
            File jmeterHome = new File("C:\\Workspace\\jmeter");
            File jmeterProperties = new File(jmeterHome.getPath() + "/bin/jmeter.properties");


            // Initialize Properties, logging, locale, etc.
            JMeterUtils.setJMeterHome(jmeterHome.getPath());
            JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
            JMeterUtils.initLocale();

            // Set directory for HTML report
            File repDir = new File("./HTMLReport");
            JMeterUtils.setProperty("jmeter.reportgenerator.exporter.html.property.output_dir",repDir.getPath());

            //clear jmeter output directory
            try{
                File repDirPath = new File("./report-output");
                deleteDirectory(repDirPath);
                deleteDirectory(repDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //Initialize local variables
            jmeter = new StandardJMeterEngine();
            testPlanHarshTree = new ListedHashTree();
            logFile = new File("./perfomanceLog.jtl");

        }

        public void addCookieManager(String cookieName){
            // Cookie Manager
            CookieManager cookieManager = new CookieManager();
            cookieManager.setName("Cookie Manager");
            cookieManager.setProperty(TestElement.TEST_CLASS, CookieManager.class.getName());
            cookieManager.setProperty(TestElement.GUI_CLASS, CookiePanel.class.getName());
        }

        public void setConfigFile(){
            // CSV Data Set Config
            CSVDataSet csvDataSet = new CSVDataSet();
//            csvDataSet.setProperty("filename", jmeterHome.getPath() + slash +"data.csv");
            csvDataSet.setProperty("delimiter", ",");
            csvDataSet.setProperty("variableNames", "domain,User,Pass");
            //csvDataSet.setRecycle(true);
            //csvDataSet.setStopThread(false);
            csvDataSet.setName("CSV Data");
            csvDataSet.setProperty("shareMode", "shareMode.all");
            csvDataSet.setProperty(TestElement.TEST_CLASS, CSVDataSet.class.getName());
            csvDataSet.setProperty(TestElement.GUI_CLASS, TestBeanGUI.class.getName());
        }

        public void createTestPlan(JSONObject testPlanData) {
            // Test Plan
            testPlan = new TestPlan(testPlanData.get("name").toString());
            testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
            testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
            testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
            testPlanHarshTree.add(testPlan);
        }

        public void addConstantTimer(String timerName, String delay){
            // Constant Timer
            ConstantTimer constantTimer = new ConstantTimer();
            constantTimer.setProperty(TestElement.TEST_CLASS, ConstantTimer.class.getName());
            constantTimer.setProperty(TestElement.GUI_CLASS, ConstantTimerGui.class.getName());
            constantTimer.setName(timerName);
            constantTimer.setEnabled(true);
            constantTimer.setDelay(delay);
            testPlanHarshTree.add(testPlan, constantTimer);
        }

        public LoopController addLoopController(int loops){
            // Loop Controller
            LoopController loopController = new LoopController();
            loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
            loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
            loopController.setLoops(1);
            loopController.setFirst(true);
            loopController.initialize();

            return loopController; //Used in threadGroup
        }

        public void createThreadGroup(JSONObject threadGroud) {
            // Define Thread Group
            ThreadGroup threadG = new ThreadGroup();
            threadG.setName(threadGroud.get("name").toString());
            threadG.setNumThreads(((Long) threadGroud.get("threads")).intValue()); // Number of concurrent users
            threadG.setRampUp(((Long) threadGroud.get("rampUp")).intValue()); // Ramp-up period (in seconds)
            threadG.setDuration(((Long) threadGroud.get("holdTime")).intValue()); // Hold time (in seconds)
            threadG.setSamplerController(new LoopController());
            threadG.setSamplerController(addLoopController(1));

            threadGroupHashTree = testPlanHarshTree.add(testPlan,threadG);
        }

        public void createSampler(String name, String comment,String domain, String path, String method) {
            // Define HTTP Sampler
            HTTPSampler sampler = new HTTPSampler();
            sampler.setName(name);
            sampler.setComment(comment);
            sampler.setDomain(domain);

            sampler.setPath(path);
            sampler.setMethod(method);
            threadGroupHashTree.add(sampler);
        }

        public void addAssertion(){
            // Response Assertion
            ResponseAssertion assertion = new ResponseAssertion();
            assertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
            assertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
            assertion.setName("Response Assertion");
            assertion.setEnabled(true);
            assertion.setTestFieldResponseData();
            assertion.addTestString("hello");
        }

        public void createMultipleSamplers(List<JSONObject> samplers){
            for (JSONObject sampler: samplers){
                createSampler(sampler.get("title").toString(), sampler.get("comment").toString(), sampler.get("domain").toString(), sampler.get("path").toString(), sampler.get("method").toString());
            }
        }

        public void saveFile() throws IOException {
            // save generated test plan to JMeter's .jmx file format
            String saveLocation = "./example.jmx";
            SaveService.saveTree(testPlanHarshTree, new FileOutputStream(jmeter + "/example.jmx"));
        }

        public void addSummaryReporter() throws ConfigurationException {
            //add Summarizer output to get test progress in stdout like:
            // summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
            Summariser summer = null;
            String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
            if (summariserName.length() > 0) {
                summer = new Summariser(summariserName);
            }



            //delete log file if exists
            if (logFile.exists()){
                boolean delete = logFile.delete();
                System.out.println("Jtl deleted: "+delete);
            }
            ResultCollector logger = new ResultCollector(summer);
            reportGenerator = new ReportGenerator(logFile.getPath(), logger); //creating ReportGenerator for creating HTML report
            logger.setFilename(logFile.getPath());
            testPlanHarshTree.add(testPlanHarshTree.getArray()[0], logger);
        }

        public void runTestPlan() {
            // Set the test plan hash tree to the jmeter engine
            jmeter.configure(testPlanHarshTree);
            // Run Test Plan
            jmeter.run();
        }

        public void generateReportAfterRun(String testPlanId) throws GenerationException {
            reportGenerator.generate();

            ReportProcessor reportProcessor = new ReportProcessor();
            DB db = new DB();

            try{
                JSONArray summary = reportProcessor.readJsonFile("./report-output/statistics.json");
                db.storeReport(summary, testPlanId, "performanceSummary");

                List<Map<String, String>> records = reportProcessor.readJTLFile(logFile.getPath());
                JSONArray formattedReport = reportProcessor.convertToJson(records);
                db.storeReport(formattedReport, testPlanId, "performanceReports");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            //Add code to write summary to DB
        }

    }

