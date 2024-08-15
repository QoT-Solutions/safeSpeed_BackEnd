package server.perfomanceController.paramClasses;

public class ThreadGroupBody {
    //DatabaseController.insertNewThreadGroups(UserId, testPlanId, comments, title, threads, rampUp, holdTime);

    private String userId;
    private String testPlanId;
    private String comments;
    private String name;
    private int threads;
    private int rampUp;
    private int holdTime;

    public String getUserId() {
        return userId;
    }

    public String getTestPlanId() {
        return testPlanId;
    }

    public String getComments() {
        return comments;
    }

    public String getName() {
        return name;
    }

    public int getThreads() {
        return threads;
    }

    public int getRampUp() {
        return rampUp;
    }

    public int getHoldTime() {
        return holdTime;
    }
}
