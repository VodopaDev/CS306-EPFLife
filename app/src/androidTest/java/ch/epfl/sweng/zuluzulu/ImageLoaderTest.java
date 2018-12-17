package ch.epfl.sweng.zuluzulu;

import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.utility.ImageLoader;

@RunWith(AndroidJUnit4.class)
public class ImageLoaderTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test(expected = IllegalArgumentException.class)
    public void nullContextThrowsExceptionForUri(){
        ImageLoader.loadUriIntoImageView(null, Uri.parse("lol"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContainerThrowsExceptionForUri(){
        ImageLoader.loadUriIntoImageView(null, Uri.parse("lol"), mActivityRule.getActivity().getCurrentFragment().getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUriThrowsExceptionForUri(){
        ImageLoader.loadUriIntoImageView(null, null, mActivityRule.getActivity().getCurrentFragment().getContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContextThrowsException(){
        ImageLoader.loadDrawableIntoImageView(null, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContainerThrowsException(){
        ImageLoader.loadDrawableIntoImageView(null, 1, mActivityRule.getActivity().getCurrentFragment().getContext());
    }
}
