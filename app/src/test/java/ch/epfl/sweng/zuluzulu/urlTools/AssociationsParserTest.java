package ch.epfl.sweng.zuluzulu.urlTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class AssociationsParserTest {
    AssociationsParser parser;

    @Before
    public void init() {
        this.parser = new AssociationsParser();
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


    @Test
    public void parseAssociationsDataWithNull() {
        assertThat(parser.parse(null), is(nullValue()));
    }

    @Test
    public void parseAssociationsDataWithClosedBf() {
        BufferedReader bf = new BufferedReader(new StringReader(""));
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> result = parser.parse(bf);

        assertThat(result, is(nullValue()));
    }

}