package ch.epfl.sweng.zuluzulu;

import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.zuluzulu.utility.ImageLoader;

@RunWith(AndroidJUnit4.class)
public class ImageLoaderTest {

    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private class LoadUriTest{

        @Test(expected = IllegalArgumentException.class)
        public void nullContextThrowsException(){
            ImageLoader.loadUriIntoImageView(null, Uri.parse("lol"), null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullContainerThrowsException(){
            ImageLoader.loadUriIntoImageView(null, Uri.parse("lol"), mActivityRule.getActivity().getBaseContext());
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullUriThrowsException(){
            ImageLoader.loadUriIntoImageView(null, null, mActivityRule.getActivity().getBaseContext());
        }

    }

    private class LoadDrawableTest{

        @Test(expected = IllegalArgumentException.class)
        public void nullContextThrowsException(){
            ImageLoader.loadDrawableIntoImageView(null, 1, null);
        }

        @Test(expected = IllegalArgumentException.class)
        public void nullContainerThrowsException(){
            ImageLoader.loadDrawableIntoImageView(null, 1, mActivityRule.getActivity().getBaseContext());
        }

    }

}
