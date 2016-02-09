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
import javax.swing.JComboBox;
import javax.swing.JLabel;

import oss.jthinker.util.UIColor;
import oss.jthinker.util.Colors;

/**
 * Combo-box that allows to choose a color.
 * 
 * @author iappel
 */
public class JColorComboBox extends JComboBox {
    private class JColorLabel extends JLabel {
        final String _colorName;
        final Color _value;

        public JColorLabel(Color c, String colorName) {
            super(colorName);
            _value = c;
            _colorName = colorName;
        }

        @Override
        public String toString() {
            return _colorName;
        }
    }

    /**
     * Creates a new instance of JColorComboBox.
     */
    public JColorComboBox() {
        for (UIColor c: Colors.getColors()) {
            addItem(c.getColor(), c.getTitle());
        }
    }

    private void addItem(Color c, String name) {
        addItem(new JColorLabel(c, name));
    }

    /**
     * Returns the color that corresponds to selected item.
     * 
     * @return color that corresponds to selected item.
     */
    public Color getItemColor() {
        Object obj = getSelectedItem();

        if (obj instanceof JColorLabel) {
            return ((JColorLabel) obj)._value;
        } else {
            return null;
        }
    }

    /**
     * Selects the item that corresponds to given color.
     * 
     * @param c color that corresponds to item that should be selected.
     */
    public void setItemColor(Color c) {
        for (int i = 0; i < getItemCount(); i++) {
            Object obj = this.getItemAt(i);
            if (obj instanceof JColorLabel) {
                JColorLabel cl = (JColorLabel) obj;
                if (cl._value.equals(c)) {
                    this.setSelectedItem(obj);
                    return;
                }
            }
        }
    }
}
