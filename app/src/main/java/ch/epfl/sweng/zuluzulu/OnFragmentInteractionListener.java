package ch.epfl.sweng.zuluzulu;

import ch.epfl.sweng.zuluzulu.Structure.User;

public interface OnFragmentInteractionListener<T> {

    void onFragmentInteraction(String tag, T data);
    void passUser(User user);
}
