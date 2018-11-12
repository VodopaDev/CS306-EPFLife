package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;

import static org.junit.Assert.*;

public class UrlReaderTest {
    UrlReader reader;

    @Before
    public void setUp(){
        this.reader = new UrlReader();
    }


    @Test
    public void canReadGoodUrl(){
        BufferedReader result = this.reader.read("http://example.com");
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
}