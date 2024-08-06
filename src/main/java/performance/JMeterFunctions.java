package performance;


import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class JMeterFunctions {

        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        public static void main(String[] args) {
            JMeterFunctions jMeterFunctions = new JMeterFunctions();

            HashTree testPlanHashTree = jMeterFunctions.buildTestPlan();
            jMeterFunctions.runTestPlan(testPlanHashTree);
        }

        public TestPlan createTestPlan(String name) {
            // Define Test Plan
            TestPlan testPlan = new TestPlan("Sample Test Plan");
            return testPlan;
        }
        


        public ThreadGroup createThreadGroup(String name, int numThreads, int rampUp) {
            // Define Thread Group
            ThreadGroup threadGroup = new ThreadGroup();
            threadGroup.setName(name);
            threadGroup.setNumThreads(numThreads); // Number of concurrent users
            threadGroup.setRampUp(rampUp); // Ramp-up period (in seconds)
            threadGroup.setSamplerController(new LoopController());

            return threadGroup;
        }

//        public HTTPSampler createSampler(String name, String comment,String domain, String path, String method)
        public HTTPSampler createSampler(String name, String comment,String domain, String path, String method) {
            // Define HTTP Sampler
            HTTPSampler sampler = new HTTPSampler();
            sampler.setName(name);
            sampler.setComment(comment);
            sampler.setDomain(domain);
            sampler.setPath(path);
            sampler.setMethod(method);

            return sampler;
        }

        public HashTree buildTestPlan(){
            TestPlan testPlan = createTestPlan("Sample Test Plan");
            ThreadGroup threadGroup = createThreadGroup("Sample Thread Group", 1, 1);
            ArrayList<JSONObject> samples = new ArrayList<JSONObject>(){{
                add(new JSONObject(){{
                    put("name", "Get qot solution");
                    put("comment", "Sample HTTP Request 1");
                    put("domain", "https://www.qotsolutions.co.za/");
                    put("path", "/");
                    put("method", "GET");
                }});

                add(new JSONObject(){{
                    put("name", "Get google");
                    put("comment", "Sample HTTP Request 2");
                    put("domain", "http://www.google.com");
                    put("path", "/");
                    put("method", "GET");
                }});
            }};

            // Add samplers to thread group
            HashTree threadGroupHashTree = new HashTree();
            for (JSONObject sample : samples) {
                String name = sample.get("name").toString();
                String comment = sample.get("comment").toString();
                String domain = sample.get("domain").toString();
                String path = sample.get("path").toString();
                String method = sample.get("method").toString();
                HTTPSampler sampler = createSampler(name, comment, domain, path, method);
                threadGroupHashTree.add(threadGroup, sampler);
            }

            // Add Thread Group to Test Plan
            HashTree testPlanHashTree = new HashTree();
            testPlanHashTree.add(testPlan, threadGroupHashTree);

            return testPlanHashTree;
        }


        public void runTestPlan(HashTree testPlanHashTree) {
            // Set the test plan hash tree to the jmeter engine
            jmeter.configure(testPlanHashTree);

            // Run Test Plan
            jmeter.run();

        }

    }

