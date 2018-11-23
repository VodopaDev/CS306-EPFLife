package ch.epfl.sweng.zuluzulu.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

public enum PostColor {

    RED("#FF0000"),
    GREEN("#32CD32"),
    BLUE("#1E90FF"),
    ORANGE("#FFA500"),
    YELLOW("#F0E68C"),
    PURPLE("#EE82EE");

    private String value;

    PostColor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
     *
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
}
