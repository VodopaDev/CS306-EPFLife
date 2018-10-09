package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.runner.RunWith;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.View.AssociationCard;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class AssociationCardTest {

    private AssociationCard card;
    private Association asso = mock(Association.class);


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


}
