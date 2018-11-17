package ch.epfl.sweng.zuluzulu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.User;

public class DynamicToolbar extends Toolbar {
    ConstraintLayout eventLayout;
    ConstraintLayout associationLayout;
    ConstraintLayout baseLayout;

    Event currentEvent;
    Association currentAssociation;
    User currentUser;

    public DynamicToolbar(Context context) {
        super(context);
        eventLayout = findViewById(R.id.toolbar_event);
        associationLayout = findViewById(R.id.toolbar_association);
        baseLayout = findViewById(R.id.toolbar_base);
    }

    public DynamicToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        eventLayout = findViewById(R.id.toolbar_event);
        associationLayout = findViewById(R.id.toolbar_association);
        baseLayout = findViewById(R.id.toolbar_base);
    }

    public DynamicToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        eventLayout = findViewById(R.id.toolbar_event);
        associationLayout = findViewById(R.id.toolbar_association);
        baseLayout = findViewById(R.id.toolbar_base);
    }


    public void switchToEventLayout(Event event){
        baseLayout.setVisibility(GONE);
        associationLayout.setVisibility(GONE);
        eventLayout.setVisibility(VISIBLE);
    }

    public void switchToAssociationLayout(Association association){
        baseLayout.setVisibility(GONE);
        associationLayout.setVisibility(VISIBLE);
        eventLayout.setVisibility(GONE);
    }

    public void switchToBaseLayout(){
        baseLayout.setVisibility(VISIBLE);
        associationLayout.setVisibility(GONE);
        eventLayout.setVisibility(GONE);
    }
}
