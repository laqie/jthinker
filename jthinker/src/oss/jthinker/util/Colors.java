package oss.jthinker.util;

import java.awt.Color;

import oss.jthinker.util.UIColor;

/**
 * Created by arello on 09/02/16.
 */
public class Colors {


    public final static UIColor WHITE = new UIColor("white", new Color(255, 255, 255));
    public final static UIColor RED = new UIColor("red", new Color(243, 121, 137));
    public final static UIColor PINK = new UIColor("pink", new Color(250, 170, 181));
    public final static UIColor YELLOW = new UIColor("yellow", new Color(247, 254, 150));
    public final static UIColor GREEN = new UIColor("green",  new Color(139, 234, 175));
    public final static UIColor CYAN = new UIColor("cyan", new Color(131, 221, 214));
    public final static UIColor BLUE = new UIColor("blue", new Color(144, 192, 216));


    public final static UIColor[] colors = {
            WHITE,
            RED,
            PINK,
            YELLOW,
            CYAN,
            GREEN,
            BLUE,
    };


    public static UIColor[] getColors() {
        return colors;
    }

    public static String toString(Color color) {
        String result = WHITE.getName();

        for (UIColor c: colors) {
            if (color.equals(c.getColor())) {
                result = c.getName();
                break;
            }
        }
        return result;
    }

    public static Color fromString(String name) {
        Color result = WHITE.getColor();
        for (UIColor c: colors) {
            if (name.equals(c.getName())) {
                result = c.getColor();
                break;
            }
        }
        return result;
    }
}
