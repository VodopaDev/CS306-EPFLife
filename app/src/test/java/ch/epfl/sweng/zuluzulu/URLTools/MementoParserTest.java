package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class MementoParserTest {
    MementoParser parser;

    @Before
    public void init(){
        this.parser = new MementoParser();
    }

    @Test
    public void parseMementoData() {
        List<String> result = parser.parse(new BufferedReader(new StringReader("[{}]")));
        assertNotNull(result);
        assertTrue(result.size() == 1);
    }


    @Test
    public void parseMementoDataWithNull() {
        assertThat(parser.parse(null), is(nullValue()));
    }

    @Test
    public void parseMementoDataWithClosedBf() {
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