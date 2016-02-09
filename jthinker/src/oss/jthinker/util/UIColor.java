package oss.jthinker.util;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class UIColor {
    Color _color;
    String _name;

    public UIColor(String name, Color color) {
        _name = name;
        _color = color;
    }

    public String getTitle() {
        return toTitleCase(_name);
    }

    public String getName() {
        return _name;
    }

    public Color getColor() {
        return _color;
    }

    @NotNull
    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

}