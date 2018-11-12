package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class IconParserTest {
    IconParser parser;

    @Before
    public void init(){
        this.parser = new IconParser();
    }


    @Test
    public void parseIconWithNull() {
        assertNull(parser.parse(null));
    }

    @Test
    public void parseIconWrongData() {
        List<String> result = parser.parse(new BufferedReader(new StringReader("test")));
        assertNotNull(result);
        assertTrue(result.size() == 0);
    }

    @Test
    public void parseIcon() {
        List<String> result = parser.parse(new BufferedReader(
                new StringReader("<link href=\"my.ico\" />")));
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test
    public void parseIconWithWronInputStream() {
        StringReader input = new StringReader("input");
        input.close();
        List<String> result = parser.parse(new BufferedReader(
                input));
        assertNull(result);
    }
}