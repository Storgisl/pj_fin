package tests;

import service.AuthService;
import model.User;

public class TestAuthService {
    public static void main(String[] args) {
        AuthService auth = new AuthService();
        boolean r1 = auth.register("testuser", "123");
        assert r1 : "Registration failed";

        User u = auth.login("testuser", "123");
        assert u != null : "Login failed";

        User u2 = auth.login("testuser", "wrong");
        assert u2 == null : "Login should fail with wrong password";

        System.out.println("AuthService tests passed.");
    }
}

