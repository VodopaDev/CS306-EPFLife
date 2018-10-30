package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import ch.epfl.sweng.zuluzulu.tequila.AuthClient;
import ch.epfl.sweng.zuluzulu.tequila.HttpUtils;
import ch.epfl.sweng.zuluzulu.tequila.OAuth2Config;

@RunWith(JUnit4.class)
public class AuthClientTest {
    private OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, "b7b4aa5bfef2562c2a3c3ea6@epfl.ch", "15611c6de307cd5035a814a2c209c115", "epflife://login");
    private String code = "1234";
    private String redirecUri = "ImARedirectUri&code=1234";
    @Test
    public void creadeCodeRequestUrlIsWorking() throws Exception{
        String url = AuthClient.createCodeRequestUrl(config);
        if(!url.equals("https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + config.scopes[0])){
            throw new Exception("Url is not equal");
        }
    }

    @Test
    public void extractCodeCorrectly() throws Exception{
        String extractedCode = AuthClient.extractCode(redirecUri);
        if (!extractedCode.equals("1234")){
            throw new Exception("Code is not properly extracted");
        }
    }

}
