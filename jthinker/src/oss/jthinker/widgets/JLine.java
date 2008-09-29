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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JComponent;

/**
 * An UI widget that is a movable line.
 * 
 * @author iappel
 */
public class JLine extends JComponent {
    private Point _endA, _endZ;
    /**
     * orientation == true means line is drawn from upper-left corner
     * to downer-right
     * orientation == false means line is drawn from upper-right corner
     * to downer-left
     */
    private boolean orientation;
    
    // specifies, should the arrow at endZ be drawn or not
    private final boolean drawArrow;

    /**
     * Creates a new JLine instance.
     * 
     * @param a starting point
     * @param z ending point
     * @param arrow should arrow head be drawn or not
     */
    public JLine(Point a, Point z, boolean arrow) {
        _endA = a;
        _endZ = z;
        drawArrow = arrow;
        setForeground(Color.BLACK);
        updateBounds();
    }

    /**
     * Sets starting point of the line.
     * 
     * @param p starting point
     */
    public void setEndA(Point p) {
        setEnds(p, null);
    }

    /**
     * Sets ending point of the line.
     * 
     * @param p ending point
     */
    public void setEndZ(Point p) {
        setEnds(null, p);
    }

    /**
     * Sets both points of the line.
     * 
     * @param a starting point
     * @param z ending point
     */
    public void setEnds(Point a, Point z) {
        boolean flagA = false, flagZ = false;

        if ((a != null) && !a.equals(_endA)) {
            flagA = true;
            _endA = a;
        }

        if ((z != null) && !z.equals(_endZ)) {
            flagZ = true;
            _endZ = z;
        }

        if (flagA || flagZ) {
            updateBounds();
        }
    }

    /**
     * Returns starting point of the line.
     * 
     * @return starting point of the line.
     */
    public Point getEndA() {
        return _endA;
    }

    /**
     * Returns starting point of the line.
     * 
     * @return starting point of the line.
     */
    public Point getEndZ() {
        return _endZ;
    }

    private void updateBounds() {
        int minx, miny, dx, dy;
        orientation = true;

        if (_endA.x < _endZ.x) {
            minx = _endA.x;
            dx = _endZ.x - minx;
        } else {
            minx = _endZ.x;
            dx = _endA.x - minx;
            orientation = !orientation;
        }
        if (_endA.y < _endZ.y) {
            miny = _endA.y;
            dy = _endZ.y - miny;
        } else {
            miny = _endZ.y;
            dy = _endA.y - miny;
            orientation = !orientation;
        }

        setBounds(minx - 5, miny - 5, dx + 10, dy + 10);
    }

    @Override
    /** {@inheritDoc} */
    public void paint(Graphics g) {
        int x = getWidth(), y = getHeight();
        g.setColor(getForeground());
        if (orientation) {
            g.drawLine(5, 5, x - 5, y - 5);
        } else {
            g.drawLine(5, y - 5, x - 5, 5);
        }
        if (drawArrow) {
            paintArrow(g);
        }
    }

    /**
     * Calculates distance from random point in widget's
     * coordinate space to line. Uses {@see GeometryUtils#distanceToLine}
     * for actual computations.
     * 
     * @param a point in widget's coordinate space
     * @return geometric distance from point to line.
     */
    public double distanceToInnerPoint(Point a) {
        Point b = new Point(a);
        b.translate(getX(), getY());
        return distanceToPoint(b);
    }

    /**
     * Calculates distance from random point in widget parent's
     * coordinate space to line. Uses {@see GeometryUtils#distanceToLine}
     * for actual computations.
     * 
     * @param a point in widget parent's coordinate space
     * @return geometric distance from point to line.
     */    
    public double distanceToPoint(Point a) {
        return GeometryUtils.distanceToLine(a, _endA, _endZ);
    }

    // Draws an arrowhead at endZ
    private void paintArrow(Graphics g) {
        Point endA = this.getEndA().getLocation();
        endA.translate(-getX(), -getY());
        Point endZ = this.getEndZ().getLocation();
        endZ.translate(-getX(), -getY());
        double x = endA.getX() - endZ.getX();
        double y = endA.getY() - endZ.getY();
        double xy = Math.sqrt(x * x + y * y);
        double dx = x / xy;
        double dy = y / xy;
        double x0 = endZ.getX() + 15 * dx, y0 = endZ.getY() + 15 * dy;
        double x1 = x0 + 4 * dy, y1 = y0 - 4 * dx;
        double x2 = x0 - 4 * dy, y2 = y0 + 4 * dx;
        Polygon poly = new Polygon();
        poly.addPoint(endZ.x, endZ.y);
        poly.addPoint((int) (x1 + 0.5), (int) (y1 + 0.5));
        poly.addPoint((int) (x2 + 0.5), (int) (y2 + 0.5));
        g.fillPolygon(poly);
    }
}
