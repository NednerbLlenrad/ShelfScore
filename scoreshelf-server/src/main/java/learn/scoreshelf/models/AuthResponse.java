package learn.scoreshelf.models;

public class AuthResponse {

    private String token;
    private AppUserResponse appUser;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUserResponse getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserResponse appUser) {
        this.appUser = appUser;
    }
}
