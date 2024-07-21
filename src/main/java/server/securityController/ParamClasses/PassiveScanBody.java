package server.securityController.ParamClasses;

public class PassiveScanBody {
    String BaseUrl;
    String title;
    String recursive;
    String userId;

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String name) {
        this.BaseUrl = name;
    }


}
