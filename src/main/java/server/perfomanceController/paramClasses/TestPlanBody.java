package server.perfomanceController.paramClasses;

public class TestPlanBody {

    //(userId, title, comments);
    private String userId;
    private String title;
    private String comments;

    public String getUserId() {
        return userId;
    }

    public String getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }
}
