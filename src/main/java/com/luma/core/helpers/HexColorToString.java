package com.luma.core.helpers;

import java.util.HashMap;
import java.util.Map;

public class HexColorToString {
    private static final Map<String, String> colorMap = new HashMap<>();

    static {
        colorMap.put("000000", "Black");
        colorMap.put("FFFFFF", "White");
        colorMap.put("FF0000", "Red");
        colorMap.put("00FF00", "Lime");
        colorMap.put("0000FF", "Blue");
        colorMap.put("FFFF00", "Yellow");
        colorMap.put("00FFFF", "Cyan");
        colorMap.put("FF00FF", "Magenta");
    }

    public static String getColorName(String hexColor) {
        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        hexColor = hexColor.toUpperCase();

        return colorMap.getOrDefault(hexColor, "Unknown");
    }
}
