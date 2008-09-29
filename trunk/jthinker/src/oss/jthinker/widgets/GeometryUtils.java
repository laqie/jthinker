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

import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 * Helper functions for computing and adjusting locations of
 * UI widgets.
 * 
 * @author iappel
 */
public class GeometryUtils {
    /**
     * Calculates location of component's center (in coordinates of
     * component's parent)
     * 
     * @param c
     * @return component center location
     */
    public static Point computeCenterPoint(Component c) {
        Rectangle r = c.getBounds();
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }
    
    /**
     * Sets component's center point to provided coordinate.
     * General contract is that {@link computeCenterPoint(Component)} call
     * right after setting center must return the very same coordinate as
     * one that was set.
     * 
     * @param c component to move
     * @param p point to set as center (in parent's coordinates)
     */
    public static void setCenterPoint(Component c, Point p) {
        int dw = c.getWidth() / 2;
        int dh = c.getHeight() / 2;
        c.setLocation(p.x - dw, p.y - dh);
    }
    
    /**
     * Similar to {@link setCenterPoint(Component, Point)} but operates
     * with component's own point of origin instead of parent's.
     * 
     * @param c component to move
     * @param p
     */
    public static void adjustCenterPoint(Component c, Point p) {
        Point l = c.getLocation();
        l.translate(p.x - c.getWidth() / 2, p.y - c.getHeight() / 2);
        c.setLocation(l);
    }    
    
    /**
     * Computes distance from a point on a plane to line given by two points.
     * 
     * @param p point, distance from which is to be calculated
     * @param endA one end of the line
     * @param endZ the other end of the line
     * @return distance from p to line, formed by endA and endZ
     */
    public static double distanceToLine(Point2D p, Point2D endA, Point2D endZ) {
        /* Geometry here is:
         * - A and B are points on ends of the line
         * - C is a point, distance from which to AB is to be calculated
         * - D is a point on AB, such that CD is perpendicular to AB.
         * We need to find length of CD. Composing a system of equations:
         * - AC squared = AD squared + CD squared (ACD is a right triangle)
         * - BC squared = BD squared + CD squared (BCD is also a right triangle)
         * - AD + BD = AB (D is on AB)
         * it's solution is obvious from the code :-)
        */
        double AC = p.distance(endA);
        double BC = p.distance(endZ);
        double AB = endA.distance(endZ);
        if (AB == (AC + BC)) {
            return 0;
        }
        double ACs = AC * AC;
        double BCs = BC * BC;
        double AD_BD = (ACs - BCs) / AB;
        double AD = (AD_BD + AB) / 2;
        double CDs = ACs - (AD * AD);
        return Math.sqrt(CDs);
    }

    /**
     * Calculates the length of hypotenuse of right triangle from
     * given legs.
     * 
     * @param leg1 lenght of the first leg
     * @param leg2 length of the other leg
     * @return length of hypotenuse
     */
    public static double hypotenuse(double leg1, double leg2) {
        return Math.sqrt((leg1 * leg1) + (leg2 * leg2));
    }
    
    /**
     * Calculates sine of angle between first leg and hypotenuse
     * in right triangle with given legs.
     * 
     * @param leg1 length of the first leg (that forms the angle)
     * @param leg2 length of the other leg
     * @return sine of angle between first leg and hypotenuse.
     */
    public static double triangleSin(double leg1, double leg2) {
        return leg2 / hypotenuse(leg1, leg2);
    }
    
    /**
     * Calculates sinus of angle between first leg and hypotenuse
     * in right triangle with given legs.
     * 
     * @param p point, x coordinate of which is treated as length of
     * aligned leg and y coordinate - as the other leg
     * @return angle between first leg and hypotenuse
     */
    public static double triangleSin(Point p) {
        return triangleSin(p.x, p.y);
    }

    /**
     * Calculates cosine of angle between first leg and hypotenuse
     * in right triangle with given legs.
     * 
     * @param leg1 length of the first leg (that forms the angle)
     * @param leg2 length of the other leg
     * @return cosine of angle between first leg and hypotenuse.
     */
    public static double triangleCos(double leg1, double leg2) {
        return leg1 / hypotenuse(leg1, leg2);
    }
    
    /**
     * Calculates sinus of angle between first leg and hypotenuse
     * in right triangle with given legs.
     * 
     * @param p point, x coordinate of which is treated as length of
     * aligned leg and y coordinate - as the other leg
     * @return angle between first leg and hypotenuse
     */
    public static double triangleCos(Point p) {
        return triangleCos(p.x, p.y);
    }

    /**
     * Calculates the intersection point of rectangle's border and line
     * between rectangle's center and some outer point.
     * 
     * @param area rectangle's position
     * @param outPoint outer point location
     * @return intersection point of rectangle's border and line
     * between rectangle's center and some outer point.
     */
    public static Point rectangleIntersection(Rectangle area, Point outPoint) {
        Point bordA = new Point(area.x, area.y);
        Point bordZ = bordA.getLocation();
        bordZ.translate(area.width, area.height);
        if ( (bordA.x < outPoint.x) && (outPoint.x < bordZ.x) &&
             (bordA.y < outPoint.y) && (outPoint.y < bordZ.y)) {
            // point is inside the rectangle
            return outPoint;
        }
        Point base = new Point((bordA.x + bordZ.x)/2, (bordA.y + bordZ.y)/2);
        Point size = new Point(area.width, area.height);
        Point diff = new Point(outPoint.x - base.x, outPoint.y - base.y);
        
        double sizeSin = triangleSin(size);
        double diffSin = triangleSin(diff);
        double diffCos = triangleCos(diff);
        
        double retX, retY;
        if (sizeSin < Math.abs(diffSin)) {
            // line intersects horizontal bound
            retY = (diff.y < 0) ? bordA.y : bordZ.y;
            double tmpY = retY - base.y;
            // tmpX/tmpY == diff.x/diff.y
            double tmpX = diffCos * tmpY / diffSin;
            retX = base.x + tmpX;
        } else {
            // line intersects vertical bound
            retX = (diff.x < 0) ? bordA.x : bordZ.x;
            double tmpX = retX - base.x;
            // tmpY/tmpX == diff.y/diff.x
            double tmpY = diffSin / diffCos * tmpX;
            retY = base.y + tmpY;
        }
        return new Point((int)retX, (int)retY);
    }

    /**
     * Calculates the intersection point of ellipse's border
     * and line between ellipse's center and some outer point.
     * 
     * @param area rectangle, that limits ellipse with axises aligned to
     * cartesian ones
     * @param outPoint outer point location
     * @return intersection point of ellipse's border and line between
     * ellipse's center and some outer point.
     */
    public static Point ellipseIntersection(Rectangle area, Point outPoint) {
        double radHorz = area.width / 2;
        double radVert = area.height / 2;
        double scaleX = radHorz / radVert;
        Point center = new Point(area.x + (int)radHorz, area.y + (int)radVert);
        Point diff = new Point(outPoint.x - center.x, outPoint.y - center.y);
        Point scaleDiff = new Point((int)(diff.getX() / scaleX), diff.y);
        double scaleSin = triangleSin(scaleDiff);
        double scaleCos = triangleCos(scaleDiff);
        double retX = radHorz * scaleCos + center.x;
        double retY = radVert * scaleSin + center.y;
        return new Point((int)retX, (int)retY);
    }

    /**
     * Calculates the intersection point of rectangle's border (border has
     * slightly softened corners) and line between rectangle's center and
     * some outer point.
     * 
     * @param area rectangle's position
     * @param outPoint outer point location
     * @return intersection point of rectangle's border and line
     * between rectangle's center and some outer point.
     */
    public static Point roundRectangleIntersection(Rectangle area, Point outPoint) {
        Point base = rectangleIntersection(area, outPoint);
       
        boolean onLeft = (base.x - area.x) < 6;
        boolean onRight = (area.x + area.width - base.x) < 6;
        boolean onTop = (base.y - area.y) < 6;
        boolean onBottom = (area.y + area.height - base.y) < 6;

        boolean onCorner = (onLeft || onRight) && (onTop || onBottom);
        
        if (onCorner) {
            base.x = onLeft ? area.x + 3 : area.x + area.width - 3;
            base.y = onTop ? area.y + 3 : area.y + area.height - 3;
        }
        return base;
    }

    /**
     * Calculates a hexagon, that should fit into a specified rectangle.
     * 
     * @param r rectangle to fit a hexagon
     * @return polygon that describes the required hexagon
     */
    public static Polygon computeHexagon(Rectangle r) {
        Polygon poly = new Polygon();
        
        int delta = Math.min(15, r.width / 4);
        
        final int x1 = r.x,
                x2 = r.x + delta,
                x3 = r.x + r.width - delta,
                x4 = r.x + r.width,
                y1 = r.y,
                y3 = r.y + r.height,
                y2 = (y1 + y3) / 2;
        
        poly.addPoint(x1, y2);
        poly.addPoint(x2, y1);
        poly.addPoint(x3, y1);
        poly.addPoint(x4, y2);
        poly.addPoint(x3, y3);
        poly.addPoint(x2, y3);
        
        return poly;
    }
}
