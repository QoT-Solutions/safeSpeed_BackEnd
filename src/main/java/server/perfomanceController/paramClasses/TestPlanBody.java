package server.perfomanceController.paramClasses;

public class TestPlanBody {

    private String description;
    //(userId, title, comments);
    private String userId;
    private String title;
    private String type;

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
