package ch.epfl.sweng.zuluzulu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class TestingActivity  extends AppCompatActivity implements OnFragmentInteractionListener {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void setUser(User user){this.user = user;}

    public boolean openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_test, fragment).commit();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        switch (tag) {
            case ChannelFragment.TAG:
                int channelID = (Integer) data;
                openFragment(ChatFragment.newInstance(user, channelID));
                break;
            case AssociationDetailFragment.TAG:
                Association asso = (Association)data;
                openFragment(AssociationDetailFragment.newInstance(user, asso));
                break;
            default:
                // Should never happen
                throw new AssertionError("Invalid message");
        }
    }
}
