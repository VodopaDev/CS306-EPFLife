package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParsersTest {


    @Test
    public void parseAssociationsDataWithNull() {
        assertNull(Parsers.parseAssociationsData(null));
    }

    @Test
    public void parseIconWithNull() {
        assertNull(Parsers.parseIcon(null));
    }

    @Test
    public void parseIconWrongData() {
        List<String> result = Parsers.parseIcon(new BufferedReader(new StringReader("test")));
        assertNotNull(result);
        assertTrue(result.size() == 0);
    }

    @Test
    public void parseAssociationsDataWrongData() {
        List<String> result = Parsers.parseAssociationsData(new BufferedReader(new StringReader("test")));
        assertNotNull(result);
        assertTrue(result.size() == 0);
    }

    @Test
    public void parseIcon() {
        List<String> result = Parsers.parseIcon(new BufferedReader(
                new StringReader("<link href=\"my.ico\" />")));
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test
    public void parseAssociationsData() {
        List<String> result = Parsers.parseAssociationsData(new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />")));
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test
    public void parseAssociationsDataWithWrongStream() {
        StringReader input = new StringReader("input");
        input.close();
        List<String> result = Parsers.parseAssociationsData(new BufferedReader(input));
        assertNull(result);
    }


    @Test
    public void parseIconWithWronInputStream() {
        StringReader input = new StringReader("input");
        input.close();
        List<String> result = Parsers.parseIcon(new BufferedReader(
                input));
        assertNull(result);
    }
}