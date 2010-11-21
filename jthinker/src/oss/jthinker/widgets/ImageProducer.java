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

import oss.jthinker.swingutils.WindowUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 * Class for producing png/jpeg images using a GUI component's data.
 * 
 * @author iappel
 */
public class ImageProducer {
    private final static String TITLE = "http://code.google.com/p/jthinker";
    protected static final int GAP_TOP = 5;
    protected static final int GAP_BOTTOM = 5;
    protected static final int GAP_LEFT = 5;
    protected static final int GAP_RIGHT = 5;
    private static final int GAP_TITLE = 5;
    
    private final Container _container;
    private final JLabel _title;
    private final Dimension _titleSize;

    /**
     * Initializes a new ImageProducer instance and associates with
     * a given container.
     * 
     * @param container component to grab images from
     */
    public ImageProducer(Container container) {
        _container = container;
        _title = initTitleLabel();
        _titleSize = _title.getSize();
    }

    /**
     * Initializes a title label that's attached at the bottom of
     * the image.
     * 
     * @return a title label that's attached at the bottom of
     * the image.
     */
    public static JLabel initTitleLabel() {
        JLabel label = new JLabel(TITLE);
        label.setBorder(new LineBorder(WindowUtils.getDefaultForeground()));
        label.setSize(label.getPreferredSize());
        return label;
    }

    /**
     * Computes the bounds of the area with components.
     * 
     * @return Computes the bounds of the area with components.
     */    
    public Rectangle computeFilledArea() {
        int minx = _container.getWidth(), maxx = 0;
        int miny = _container.getHeight(), maxy = 0;

        LinkedList<Component> content = new LinkedList<Component>();
        
        for (int i = 0; i < _container.getComponentCount(); i++) {
            content.add(_container.getComponent(i));
        }
        
        if (_container instanceof JBackground) {
            for (Component compo : (JBackground)_container) {
                content.add(compo);
            }
        }
        
        for (Component compo : content) {
            Rectangle rect = compo.getBounds();
            minx = Math.min(minx, rect.x);
            miny = Math.min(miny, rect.y);
            maxx = Math.max(maxx, rect.x + rect.width);
            maxy = Math.max(maxy, rect.y + rect.height);
        }

        return new Rectangle(minx, miny, maxx - minx, maxy - miny);
    }

    /**
     * Computes the origin point of the image. Origin point here
     * is the top-left point of the image presented in
     * base component's coordinate space.
     *
     * @return position of top-left point of the image
     * presented in component's coordinates
     */ 
    public Point computeOrigin() {
        Rectangle filledArea = computeFilledArea();
        Point origin = filledArea.getLocation();
        origin.translate(-GAP_LEFT, -GAP_TOP);
        return origin;
    } 

    /**
     * Computes the size of the image to be produced.
     * 
     * @param areaSize size of the area filled with components.
     * @return the size of the image to be produced.
     */    
    protected Dimension computeImageSize(Dimension areaSize) {
        int width = Math.max(areaSize.width, _titleSize.width) + GAP_LEFT +
                GAP_RIGHT;

        int height = areaSize.height + _titleSize.height + GAP_TOP +
                GAP_BOTTOM + GAP_TITLE;

        return new Dimension(width, height);
    }

    /**
     * Allocates an image and fills it with white.
     * 
     * @param area bounds of the area filled with components.
     * @return the image to paint component on
     */    
    protected BufferedImage prepareImage(Rectangle area) {
        Dimension imageSize = computeImageSize(area.getSize());
        BufferedImage image = new BufferedImage(imageSize.width,
                imageSize.height,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(WindowUtils.getDefaultBackground());
        g.fillRect(0, 0, imageSize.width, imageSize.height);
        
        return image;
    }

    /**
     * Produces an image that is the snapshot of the container.
     * 
     * @return snapshot of the container.
     */
    public BufferedImage produceImage() {
        Rectangle area = computeFilledArea();
        BufferedImage image = prepareImage(area);
        
        Graphics g = image.getGraphics();
        g.translate(GAP_LEFT - area.x, GAP_TOP - area.y);
        
        if (_container instanceof JBackground) {
            ((JBackground)_container).paintBackground(g);
        }

        Color containerBackground = _container.getBackground();
        _container.setBackground(WindowUtils.getDefaultBackground());
        _container.paintComponents(g);
        _container.setBackground(containerBackground);
        
        g = image.getGraphics();
        g.translate(GAP_LEFT, area.height + GAP_TOP + GAP_TITLE);
        _title.paint(g);

        return image;
    }

    public byte[] getRawData(String format) throws IOException {
        BufferedImage image = produceImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, format, stream);
        return stream.toByteArray();
    }

    /**
     * Produces an image and saves it as a Jpeg file to given location.
     * 
     * @param file file name to place generated file
     * @throws IOException if an error occurs during writing.
     */
    public void jpegExport(File file) throws IOException {
        BufferedImage image = produceImage();
        ImageIO.write(image, "JPEG", file);
    }

    /**
     * Produces an image and saves it as a PNG file to given location.
     * 
     * @param file file name to place generated file
     * @throws IOException if an error occurs during writing.
     */
    public void pngExport(File file) throws IOException {
        BufferedImage image = produceImage();
        ImageIO.write(image, "PNG", file);
    }

    /**
     * Produces an image and saves it to given location. Format for
     * the image file is deduced from filename's extension.
     * 
     * @param file file name to place generated file
     * @throws IOException if an error occurs during writing.
     * @throws IllegalArgumentException if file name has no valid suffix
     * @deprecated use {@link #imageExport(File)} instead
     */
    @Deprecated
    public void fileExport(File file) throws IOException, IllegalArgumentException {
        imageExport(file);
    }

    /**
     * Produces an image and saves it to given location. Format for
     * the image file is deduced from filename's extension.
     * 
     * @param file file name to place generated file
     * @throws IOException if an error occurs during writing.
     * @throws IllegalArgumentException if file name has no valid suffix
     */
    public void imageExport(File file) throws IOException, IllegalArgumentException {
        String filename = file.getName();
        if (filename.endsWith(".png")) {
            pngExport(file);
        } else if (filename.endsWith("jpeg") || filename.endsWith(".jpg")) {
            jpegExport(file);
        } else {
            String msg = "File must have either .jpeg or .png suffix";
            throw new IllegalArgumentException(msg);
        }
    }
}
