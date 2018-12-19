package ch.epfl.sweng.zuluzulu.fragments.superFragments;

import android.os.Bundle;
import ch.epfl.sweng.zuluzulu.structure.user.User;

public class FragmentWithUser<U extends User> extends SuperFragment {
    public static final String ARG_USER = "ARG_USER";
    protected U user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (U) getArguments().getSerializable(ARG_USER);
        }
    }

}
