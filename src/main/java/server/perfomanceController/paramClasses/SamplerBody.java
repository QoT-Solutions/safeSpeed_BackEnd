package server.perfomanceController.paramClasses;

public class SamplerBody {
    //DatabaseController.insertNewSampler(testPlanId, name, comments, protocol, server, type, port, body);

    private String testPlanId;
    private String name;
    private String comments;
    private String domain;
    private String path;
    private String method;


    public String getTestPlanId() {
        return testPlanId;
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}
