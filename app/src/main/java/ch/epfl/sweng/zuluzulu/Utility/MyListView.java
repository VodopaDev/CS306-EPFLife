package ch.epfl.sweng.zuluzulu.Utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * List view wrap the height : see https://stackoverflow.com/questions/11295080/android-wrap-content-is-not-working-with-listview
 */
public class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
