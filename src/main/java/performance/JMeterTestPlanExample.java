package performance;

import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jmeter.extractor.gui.RegexExtractorGui;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jmeter.timers.gui.ConstantTimerGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.ViewResultsFullVisualizer;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class JMeterTestPlanExample {

    public static void main(String [] args) throws URISyntaxException {

        File jmeterHome = new File("C:\\Workspace\\jmeter");
        if (jmeterHome.exists()) {
            File jmeterProperties = new File(jmeterHome.getPath() + "/bin/jmeter.properties");
            if (jmeterProperties.exists()) {

                StandardJMeterEngine jmeter = new StandardJMeterEngine();

                JMeterUtils.setJMeterHome(jmeterHome.getPath());
                JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
                JMeterUtils.initLocale();

                ListedHashTree hashTree = new ListedHashTree();

                // Test Plan
                TestPlan testPlan = new TestPlan("Test Plan");
                testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
                testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
                testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());
                hashTree.add(testPlan);

                // Constant Timer
                ConstantTimer constantTimer = new ConstantTimer();
                constantTimer.setProperty(TestElement.TEST_CLASS, ConstantTimer.class.getName());
                constantTimer.setProperty(TestElement.GUI_CLASS, ConstantTimerGui.class.getName());
                constantTimer.setName("Constant Timer");
                constantTimer.setEnabled(true);
                constantTimer.setDelay("5000");
                hashTree.add(testPlan, constantTimer);

                // Loop Controller
                LoopController loopController = new LoopController();
                loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
                loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
                loopController.setLoops(1);
                loopController.setFirst(true);
                loopController.initialize();

                // Thread Group
                ThreadGroup threadGroup = new ThreadGroup();
                threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
                threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
                threadGroup.setName("Thread Group");
                threadGroup.setNumThreads(1);
                threadGroup.setRampUp(1);
                threadGroup.setScheduler(false);
                threadGroup.setSamplerController(loopController);

                HashTree threadGroupHashTree = hashTree.add(testPlan, threadGroup);

                // HTTP Sampler
                HTTPSampler httpSampler = new HTTPSampler();
                httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
                httpSampler.setName("HTTP Sampler");
                httpSampler.setMethod("GET");
                httpSampler.setDomain("localhost");
                httpSampler.setPort(8090);
                httpSampler.setPath("api/echo");
                httpSampler.addArgument("message", "hello");

                HashTree httpSampleHashTree = threadGroupHashTree.add(httpSampler);

                // Response Assertion
                ResponseAssertion assertion = new ResponseAssertion();
                assertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
                assertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
                assertion.setName("Response Assertion");
                assertion.setEnabled(true);
                assertion.setTestFieldResponseData();
                assertion.addTestString("hello");

                httpSampleHashTree.add(assertion);

                // Regex Extractor
                RegexExtractor regexExtractor = new RegexExtractor();
                regexExtractor.setProperty(TestElement.TEST_CLASS, RegexExtractor.class.getName());
                regexExtractor.setProperty(TestElement.GUI_CLASS, RegexExtractorGui.class.getName());
                regexExtractor.setName("Regex Extractor");
                regexExtractor.setUseField(RegexExtractor.USE_BODY);
                regexExtractor.setRefName("data");
                regexExtractor.setRegex("(.*)");
                regexExtractor.setTemplate("$1$");
                regexExtractor.setMatchNumber(1);

                httpSampleHashTree.add(regexExtractor);

                // HTTP Sampler
                HTTPSampler httpSampler2 = new HTTPSampler();
                httpSampler2.setProperty(TestElement.TEST_CLASS, HTTPSampler.class.getName());
                httpSampler2.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
                httpSampler2.setName("HTTP Sampler");
                httpSampler2.setMethod("GET");
                httpSampler2.setDomain("localhost");
                httpSampler2.setPort(8090);
                httpSampler2.setPath("api/echo");
                httpSampler2.addArgument("message", "${data}");

                HashTree httpSampleHashTree2 = threadGroupHashTree.add(httpSampler2);


                // ...


                // Save test plan
                try {
                    SaveService.saveTree(hashTree, new FileOutputStream("example.jmx"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Summariser
                Summariser summer = null;
                String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
                if (summariserName.length() > 0) {
                    summer = new Summariser(summariserName);
                }

                // Result Collector
                String logFile = "example.jtl";
                ResultCollector resultCollector = new ResultCollector(summer);
                resultCollector.setProperty(TestElement.TEST_CLASS, ResultCollector.class.getName());
                resultCollector.setProperty(TestElement.GUI_CLASS, ViewResultsFullVisualizer.class.getName());
                resultCollector.setFilename(logFile);

                hashTree.add(testPlan, resultCollector);

                jmeter.configure(hashTree);
                jmeter.run();

            }
        }
        else{
            System.err.println("jmeter.home property is not set or pointing to incorrect location: "+ jmeterHome.getPath());
        }

    }
}