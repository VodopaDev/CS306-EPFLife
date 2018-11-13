package ch.epfl.sweng.zuluzulu.URLTools;

import org.junit.Test;

import java.io.BufferedReader;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class UrlReaderFactoryTest {

    @Test
    public void getDependency() {
        assertThat(UrlReaderFactory.getDependency(), instanceOf(UrlReader.class));
    }

    @Test
    public void setDependency() {
        UrlReaderFactory.setDependency(new UrlReader() {
            @Override
            public BufferedReader read(String name) {
                return null;
            }
        });
        assertNull(UrlReaderFactory.getDependency().read("somethine"));
    }
}