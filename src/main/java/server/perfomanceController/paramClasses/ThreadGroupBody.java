package server.perfomanceController.paramClasses;

public class ThreadGroupBody {
    //DatabaseController.insertNewThreadGroups(UserId, testPlanId, comments, title, threads, rampUp, holdTime);

    private String userId;
    private String testPlanId;
    private String comments;
    private String title;
    private String threads;
    private String rampUp;
    private String holdTime;

    public String getUserId() {
        return userId;
    }

    public String getTestPlanId() {
        return testPlanId;
    }

    public String getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getThreads() {
        return threads;
    }

    public String getRampUp() {
        return rampUp;
    }

    public String getHoldTime() {
        return holdTime;
    }
}
