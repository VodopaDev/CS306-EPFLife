package ch.epfl.sweng.zuluzulu.Utility;

import ch.epfl.sweng.zuluzulu.Structure.Utils;

public enum PostColor {

    RED(""),
    GREEN(""),
    BLUE(""),
    ORANGE(""),
    YELLOW(""),
    PURPLE("");

    private String value;

    PostColor(String value) {
        this.value = value;
    }

    public String getValue() { return value; }

    public static PostColor getRandomColor() {
        PostColor[] colors = PostColor.values();
        int randomIndex = Utils.randomInt(0, colors.length - 1);
        return colors[randomIndex];
    }
}
