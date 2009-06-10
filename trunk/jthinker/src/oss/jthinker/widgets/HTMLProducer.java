/*
 * Copyright (c) 2009, Ivan Appel <ivan.appel@gmail.com>
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
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for producing HTML files that should wrap exported JPEG/PNG files.
 * As the only usage for such producer is extension of {@link ImageProducer}'s
 * functionality, it is direct descendant of one. This is a straightforward
 * solution that may change in the future. 
 *
 * @author iappel
 */
public class HTMLProducer extends ImageProducer {
    private final List<JNode> _nodes;

    /**
     * Initializes a new HTMLProducer instance and associates with a given
     * container.
     *
     * @param container component to grab images from
     */
    public HTMLProducer(Container container) {
        super(container);
        _nodes = new LinkedList<JNode>();
        for (Component c : container.getComponents()) {
            if (c instanceof JNode) {
                _nodes.add((JNode)c);
            }
        }
    }

    /**
     * Produces an image and a wrapper HTML file and saves them to given locations.
     * Actual image export is done by {@link #imageExport(File)}, see it for
     * more info.
     *
     * @param imageFile file to export image 
     * @param htmlFile file to write HTML wrapper 
     *
     * @throws IOException if an error occurs during writing.
     */
    public void imageHtmlExport(File imageFile, File htmlFile) throws IOException {
        imageExport(imageFile);

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>jThinker diagram</title></head>\n");
        sb.append("<body bgcolor=\"#8080FF\">\n");
        sb.append("<div style=\"margin-top:120px\" align=\"center\">\n");
        sb.append("<img src=\"");
        sb.append(imageFile.getName());
        sb.append("\" usemap=\"#jthinker-export\" border=\"1\" />\n");
        sb.append("</div>");
        sb.append(renderImageMap());
        sb.append("<div>Made using <a href=\"http://code.google.com/p/jthinker\">jThinker</a></div>");
        sb.append("</body>\n</html>");
        FileWriter writer = new FileWriter(htmlFile);
        writer.write(sb.toString());
        writer.close();
    } 

    protected String renderImageMap() {
        StringBuilder sb = new StringBuilder();
        Point origin = computeOrigin(); 
        sb.append("<map name=\"jthinker-export\">\n");
        for (JNode node : _nodes) {
            String comment = node.getComment();
            if (comment.startsWith("http://")) {
                String placement = calculatePlacement(node, origin);
                sb.append("<area shape=\"rect\" coords=\"");
                sb.append(placement);
                sb.append("\" href=\"");
                sb.append(comment);
                sb.append("\" target=\"_blank\" />\n"); 
            }
        }
        sb.append("</map>\n");
        return sb.toString();
    }

    /**
     * Calculates the position of the component in coordinate space of the image.
     * 
     * @param c component, position of which is interesting
     * @param origin origin point of the image, just to avoid multiple
     *               invokes of {@link #computeOrigin()} method.
     *
     * @return position of the component as "[left],[top],[right],[bottom]"
     */
    protected static String calculatePlacement(Component c, Point origin) {
        Rectangle bounds = c.getBounds();
        int left   = bounds.x - origin.x;
        int top    = bounds.y - origin.y;

        int right  = left + bounds.width; 
        int bottom = top  + bounds.height;

        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append(",");
        sb.append(top);
        sb.append(",");
        sb.append(right);
        sb.append(",");
        sb.append(bottom);
        return sb.toString();
    }
}
