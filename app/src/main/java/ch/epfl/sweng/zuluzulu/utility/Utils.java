package ch.epfl.sweng.zuluzulu.utility;

import android.support.design.widget.Snackbar;
import android.view.View;
import java.util.Random;

import ch.epfl.sweng.zuluzulu.R;

/**
 * Interface that contains general useful functions
 */
public interface Utils {

    /**
     * Return a random integer in the range [min, max]
     *
     * @param min the smallest integer you can getAndAddOnSuccessListener
     * @param max the biggest integer you can getAndAddOnSuccessListener
     * @return the random integer
     */
    static int randomInt(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max must be bigger than min");
        }
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Draw a Snackbar
     *
     * @param container The container view
     */
    static void showConnectSnackbar(View container) {
        Snackbar.make(container, container.getContext().getString(R.string.login_for_more_features), 5000).show();
    }

}
