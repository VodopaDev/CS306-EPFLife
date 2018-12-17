package ch.epfl.sweng.zuluzulu.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PostColor {

    RED("#DD5F60"),
    GREEN("#9FC41A"),
    BLUE("#06A4CA"),
    CYAN("#8BBDB0"),
    ORANGE("#FFB900"),
    YELLOW("#FFB902");

    private String value;

    PostColor(String value) {
        this.value = value;
    }

    /**
     * Return a random postColor
     *
     * @return the random postColor
     */
    public static PostColor getRandomColor() {
        return getRandomColorButNot(null);
    }

    /**
     * Return a random postColor among all except the one given in parameter
     *
     * @param color The color we don't want
     * @return The random postColor
     */
    public static PostColor getRandomColorButNot(PostColor color) {
        List<PostColor> colors = new ArrayList<>(Arrays.asList(PostColor.values()));
        if (color != null) {
            colors.remove(color);
        }
        int randomIndex = Utils.randomInt(0, colors.size() - 1);
        return colors.get(randomIndex);
    }

    public String getValue() {
        return value;
    }
}
