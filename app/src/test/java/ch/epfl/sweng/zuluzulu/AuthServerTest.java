package ch.epfl.sweng.zuluzulu;

import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.user.User;
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
        throw new Exception("could getAndAddOnSuccessListener a user from a non valid token?");
    }

    @Test
    public void createUser(){
        String json = "{ \"Sciper\": 268785, \"authscheme\": \"OAuth2\", \"Firstname\": \"Dahn Samuel Darius\", \"Username\": \"youssefi\", \"Email\": \"dahn.youssefi@epfl.ch\", \"Name\": \"Youssefi\", \"Unit\": \"IN-BA5\", \"scope\": \"Tequila.profile\" }";
        AuthServer.createUser(new Gson().fromJson(json, AuthServer.JsonProfile.class));
        String json2 = "{ \"Sciper\": 268785, \"authscheme\": \"OAuth2\", \"Firstname\": \"Dahn Samuel Darius\", \"Username\": \"youssefi\", \"Email\": \"dahn.youssefi@epfl.ch\", \"Name\": \"Youssefi\", \"scope\": \"Tequila.profile\" }";
        AuthServer.createUser(new Gson().fromJson(json2, AuthServer.JsonProfile.class));
    }
}
