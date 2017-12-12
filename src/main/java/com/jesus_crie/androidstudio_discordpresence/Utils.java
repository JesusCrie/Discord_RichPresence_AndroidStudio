package com.jesus_crie.androidstudio_discordpresence;

import java.text.SimpleDateFormat;

public class Utils {

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public static String getTime() {
        return format.format(System.currentTimeMillis());
    }

    public static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }
}
