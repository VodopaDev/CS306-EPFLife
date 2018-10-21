package ch.epfl.sweng.zuluzulu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.Utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void longToInt(){
        List<Long> array1 = Arrays.asList(1L,2L);
        List<Integer> array2 = Utils.longListToIntList(array1);
        assertThat(Arrays.asList(1,2), equalTo(array2));
    }

}
