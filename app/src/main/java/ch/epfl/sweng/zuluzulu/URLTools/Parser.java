package ch.epfl.sweng.zuluzulu.URLTools;

import java.io.BufferedReader;

public interface Parser<T> {
    T parse(BufferedReader in);
}
