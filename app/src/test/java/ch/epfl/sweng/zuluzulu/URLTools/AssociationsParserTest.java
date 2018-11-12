package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class AssociationsParserTest {
    AssociationsParser parser;

    @Before
    public void init(){
        this.parser = new AssociationsParser();
    }


    @Test
    public void parseAssociationsDataWithNull() {
        assertNull(parser.parse(null));
    }

    @Test
    public void parseAssociationsDataWrongData() {
        List<String> result = parser.parse(new BufferedReader(new StringReader("test")));
        assertNotNull(result);
        assertTrue(result.size() == 0);
    }

    @Test
    public void parseAssociationsData() {
        List<String> result = parser.parse(new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />")));
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }

    @Test
    public void parseAssociationsDataWithWrongStream() {
        StringReader input = new StringReader("input");
        input.close();
        List<String> result = parser.parse(new BufferedReader(input));
        assertNull(result);
    }

}