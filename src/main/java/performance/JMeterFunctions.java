package performance;


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
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JMeterFunctions {

        private StandardJMeterEngine jmeter;
        private ListedHashTree testPlanHarshTree;
        private TestPlan testPlan;
        private  HashTree threadGroupHashTree;
        ReportGenerator reportGenerator;

        public JMeterFunctions() {
            // Set jmeter home
            File jmeterHome = new File("C:\\Workspace\\jmeter");
            File jmeterProperties = new File(jmeterHome.getPath() + "/bin/jmeter.properties");


            // Initialize Properties, logging, locale, etc.
            JMeterUtils.setJMeterHome(jmeterHome.getPath());
            JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
            JMeterUtils.initLocale();

            // Set directory for HTML report
            String repDir = "./HTMLReport";
            JMeterUtils.setProperty("jmeter.reportgenerator.exporter.html.property.output_dir",repDir);

            //Initialize local variables
            jmeter = new StandardJMeterEngine();
            testPlanHarshTree = new ListedHashTree();

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

        public void createTestPlan(String testPlanName) {
            // Test Plan
            testPlan = new TestPlan(testPlanName);
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

        public void createThreadGroup(String name, int numThreads, int rampUp) {
            // Define Thread Group
            ThreadGroup threadG = new ThreadGroup();
            threadG.setName(name);
            threadG.setNumThreads(numThreads); // Number of concurrent users
            threadG.setRampUp(rampUp); // Ramp-up period (in seconds)
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


            // Store execution results into a .jtl file
            File logFile = new File("./example.jtl");
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

        public void generateReportAfterRun() throws GenerationException {
            reportGenerator.generate();
        }

    }

