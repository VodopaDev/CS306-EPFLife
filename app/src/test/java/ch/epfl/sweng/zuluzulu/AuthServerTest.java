package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.tequila.AuthServer;
import ch.epfl.sweng.zuluzulu.tequila.OAuth2Config;

@RunWith(JUnit4.class)
public class AuthServerTest {
    private OAuth2Config config = new OAuth2Config(new String[]{"tequila"}, "123", "1234", "blabla");

    @Test
    public void fetchTokenReturnsAnErrorProperly() throws Exception {
        try {
            Map<String, String> token = AuthServer.fetchTokens(config, "123");
        } catch (IOException e) {
            return;
        }
        throw new Exception("could connect with a weird config???");
    }

    @Test
    public void fetchUserReturnsAnError() throws Exception {
        try {
            User user = AuthServer.fetchUser("test");
        } catch (IOException e) {
            return;
        }
        throw new Exception("could get a user from a non valid token?");
    }
}
