package server.perfomanceController.paramClasses;

public class SamplerBody {
    //DatabaseController.insertNewSampler(testPlanId, name, comments, protocol, server, type, port, body);

    private String testPlanId;
    private String name;
    private String comments;
    private String protocol;
    private String server;
    private String type;
    private String port;
    private String body;


    public String getTestPlanId() {
        return testPlanId;
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getServer() {
        return server;
    }

    public String getType() {
        return type;
    }

    public String getPort() {
        return port;
    }

    public String getBody() {
        return body;
    }


}
