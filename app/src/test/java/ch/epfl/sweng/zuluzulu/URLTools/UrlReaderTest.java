package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;

import static org.junit.Assert.*;

/*
 *This class will test the HTTP connexion. It will send network request and deppends on the network
 * So it can fail.... But still need to be tested
 */
public class UrlReaderTest {
    UrlReader reader;

    @Before
    public void setUp(){
        this.reader = new UrlReader();
    }

    @After
    public void close() {
        this.reader.disconnect();
    }

    @Test
    public void canReadGoodUrl(){
        BufferedReader result = this.reader.read("http://example.com");
        assertNotNull(result);
    }

    @Test
    public void cannotRead404(){
        BufferedReader result = this.reader.read("http://example.com/404");
        assertNull(result);
    }

    @Test
    public void canRedirectUrl(){
        BufferedReader result = this.reader.read("http://epfl.ch");
        assertNotNull(result);
    }

    @Test
    public void cannoNotExistingDomainUrl(){
        BufferedReader result = this.reader.read("http://example.not.exist");
        assertNull(result);
    }

    @Test
    public void cannotReadWrongUrl(){
        BufferedReader result = this.reader.read("exemple");
        assertNull(result);
    }

    @Test
    public void cannotReadNulll(){
        BufferedReader result = this.reader.read(null);
        assertNull(result);
    }
}