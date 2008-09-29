/*
 * Copyright (c) 2008, Ivan Appel <ivan.appel@gmail.com>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of Ivan Appel nor the names of any other jThinker
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package oss.jthinker.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.HashMap;
import javax.swing.border.LineBorder;

import static oss.jthinker.widgets.BorderType.*;

/**
 * Builder for custom borders used in jThinker.
 * 
 * @author iappel
 */
public abstract class BorderBuilder {

    private abstract static class CustomBorder extends LineBorder {

        private final int xset,  yset;

        public CustomBorder(Color c, int x, int y) {
            super(c);
            xset = x;
            yset = y;
        }

        @Override
        public final void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color componentBackground = c.getBackground();
            Color parentBackground = c.getParent().getBackground();

            g.setColor(parentBackground);
            g.fillRect(x, y, width, height);
            g.setColor(componentBackground);

            Rectangle rect = new Rectangle(x, y, width - 1, height - 1);
            cleanArea(g, rect);
            g.setColor(lineColor);
            drawBorder(g, rect);
        }

        protected abstract void cleanArea(Graphics g, Rectangle r);

        protected abstract void drawBorder(Graphics g, Rectangle r);

        @Override
        public Insets getBorderInsets(Component c) {
            Insets result = super.getBorderInsets(c);
            result.top += yset;
            result.bottom += yset;
            result.left += xset;
            result.right += xset;
            return result;
        }
    }
    private static BorderBuilder ellipticBuilder = new BorderBuilder() {

        public LineBorder createBorder(Color color) {
            return new CustomBorder(color, 10, 10) {

                @Override
                protected void cleanArea(Graphics g, Rectangle area) {
                    g.fillOval(area.x, area.y, area.width, area.height);
                }

                @Override
                protected void drawBorder(Graphics g, Rectangle area) {
                    g.drawOval(area.x, area.y, area.width, area.height);
                }
            };
        }
    };
    
    private static BorderBuilder roundedBuilder = new BorderBuilder() {
        public LineBorder createBorder(Color color) {
            return new CustomBorder(color, 0, 0) {
                int radius = 15;

                @Override
                protected void cleanArea(Graphics g, Rectangle r) {
                    g.fillRoundRect(r.x, r.y, r.width, r.height, radius, radius);
                }

                @Override
                protected void drawBorder(Graphics g, Rectangle r) {
                    g.drawRoundRect(r.x, r.y, r.width, r.height, radius, radius);
                }
            };
        }
    };
    
    private static BorderBuilder hexagonalBuilder = new BorderBuilder() {
        public LineBorder createBorder(Color color) {
            return new CustomBorder(color, 20, 0) {
                @Override
                protected void cleanArea(Graphics g, Rectangle r) {
                    g.fillPolygon(GeometryUtils.computeHexagon(r));
                }

                @Override
                protected void drawBorder(Graphics g, Rectangle r) {
                    g.drawPolygon(GeometryUtils.computeHexagon(r));
                }
            };
        }
    };
    private final static HashMap<BorderType, BorderBuilder> instances;

    static {
        instances = new HashMap<BorderType, BorderBuilder>();
        instances.put(ELLIPSE, ellipticBuilder);
        instances.put(HEXAGON, hexagonalBuilder);
        instances.put(ROUND_RECT, roundedBuilder);
        instances.put(SHARP_RECT, new BorderBuilder() {
            public LineBorder createBorder(Color color) {
                return new LineBorder(color);
            }
        });
    }

    /**
     * Returns a builder for given border type.
     * 
     * @param type type of border that builder should produce.
     * @return builder for borders of given type
     */
    public static BorderBuilder getInstance(BorderType type) {
        BorderBuilder builder = instances.get(type);
        if (builder == null) {
            String msg = "Unable to find builder of type " + type;
            throw new RuntimeException(msg);
        } else {
            return builder;
        }
    }

    /**
     * Creates a border of given color.
     * 
     * @param color
     * @return
     */
    public abstract LineBorder createBorder(Color color);

    /**
     * Creates a border of given type and color.
     * 
     * @param type type
     * @param color color
     * @return border of given type and color.
     */
    public static LineBorder createBorder(BorderType type, Color color) {
        return getInstance(type).createBorder(color);
    }
}
