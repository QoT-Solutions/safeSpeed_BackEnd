package performance;

public class main {
    public static void main(String[] args) {

        JMeterFunctions jmeter = new JMeterFunctions();

        try{
            jmeter.createTestPlan("Plan");
            jmeter.addConstantTimer("timer", "500");
            jmeter.createThreadGroup("ThreadGroup", 5, 1);
            jmeter.createSampler("Test", "nothing", "demo.guru99.com", "test/newtours", "Get");
            jmeter.addSummaryReporter();
            jmeter.runTestPlan();
            jmeter.generateReportAfterRun();
        } catch (Exception e){
            System.out.println("Something went Wrong: "+ e);
        }
    }
}
