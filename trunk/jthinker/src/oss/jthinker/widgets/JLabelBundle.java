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

import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Component for displaying multiline text as a JPanel filled with
 * JLabel's. Reason for it's use is to make a multiline text component
 * that transparently propagates mouse events to parent.
 * 
 * @author iappel
 */
public class JLabelBundle extends JPanel {
    private String textInfo;
    
    /**
     * Creates a new JLabelBundle instance.
     * 
     * @param text content to display.
     */
    public JLabelBundle(String text) {
        setAlignmentX(JPanel.CENTER_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setText(text);
    }
    
    /**
     * Sets a new textual content.
     * 
     * @param text new content to display.
     */
    public void setText(String text) {
        removeAll();
        for (String st : sliceString(text)) {
            JLabel l = new JLabel(st);
            l.setVisible(true);
            l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            add(l);
        }
        doLayout();
        textInfo = text;
    }
    
    /**
     * Slices string into a list of space-separated strings, each of which
     * is not less than 20 characters.
     * 
     * @param source string to slice
     * @return list sliced list of substrings.
     */
    public static LinkedList<String> sliceString(String source) {
        LinkedList<String> result = new LinkedList<String>();
        while (source != null) {
            int split = source.indexOf(" ", 20);
            result.add((split == -1) ? source : source.substring(0, split));
            source = (split == -1) ? null : source.substring(split+1);
        }
        return result;
    }

    /**
     * Returns the text that component currently displays (not sliced).
     *
     * @return text that component currently displays.
     */
    public String getText() {
        return textInfo;
    }
}
