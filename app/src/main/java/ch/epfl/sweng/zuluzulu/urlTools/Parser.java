package ch.epfl.sweng.zuluzulu.urlTools;

import java.io.BufferedReader;

public interface Parser<T> {
    T parse(BufferedReader in);
}
