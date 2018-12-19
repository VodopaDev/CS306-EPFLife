package ch.epfl.sweng.zuluzulu.fragments.superFragments;

import android.os.Bundle;

import ch.epfl.sweng.zuluzulu.structure.FirebaseStructure;
import ch.epfl.sweng.zuluzulu.structure.user.User;

public abstract class FragmentWithUserAndData<U extends User, D extends FirebaseStructure> extends FragmentWithUser<U> {
    public static final String ARG_DATA = "ARG_DATA";
    protected D data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (D) getArguments().getSerializable(ARG_DATA);
        }
    }
}
