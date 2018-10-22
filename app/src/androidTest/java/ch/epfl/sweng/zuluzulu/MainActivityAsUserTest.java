package ch.epfl.sweng.zuluzulu;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityAsUserTest extends TestWithLogin{


    @Test
    public void isAuthenticated() {
        // check not authenticated
        assertTrue(mActivityRule.getActivity().isAuthenticated());
    }
}