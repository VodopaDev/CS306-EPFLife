package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CalendarView;

public class HighlightedCalendarView extends CalendarView {
    public HighlightedCalendarView(@androidx.annotation.NonNull @NonNull Context context) {
        super(context);
    }

    public HighlightedCalendarView(@androidx.annotation.NonNull @NonNull Context context, @androidx.annotation.Nullable @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HighlightedCalendarView(@androidx.annotation.NonNull @NonNull Context context, @androidx.annotation.Nullable @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HighlightedCalendarView(@NonNull Context context@Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
