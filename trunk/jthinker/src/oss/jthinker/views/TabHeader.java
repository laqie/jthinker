/*
 * Copyright (c) 2010, Ivan Appel <ivan.appel@gmail.com>
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

package oss.jthinker.views;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import oss.jthinker.swingutils.ClickAdapter;
import oss.jthinker.util.Trigger;
import oss.jthinker.util.TriggerEvent;
import oss.jthinker.util.TriggerListener;

/**
 *
 * @author iappel
 */
public class TabHeader extends JPanel implements TriggerListener<String> {
    private final DiagramDeck _diagramDeck;
    private final DiagramPane _diagramPane;
    private final JLabel _titleLabel;

    public TabHeader(DiagramPane pane, DiagramDeck deck) {
        super(new BorderLayout());
        _diagramDeck = deck;
        _diagramPane = pane;
        
        Trigger<String> trigger = pane.makeTitleTrigger();
        _titleLabel = new JLabel(trigger.getState());
        _titleLabel.setAlignmentY(0.0f);
        add(_titleLabel, BorderLayout.WEST);
        JLabel crossLabel = new JLabel("[x]");
        crossLabel.addMouseListener(new ClickAdapter() {
            public void mouseRightClicked(MouseEvent me) {
            }

            public void mouseLeftClicked(MouseEvent me) {
                if (!_diagramPane.askedSave()) {
                    return;
                }
                int index = _diagramDeck.getDiagramIndex(_diagramPane);
                _diagramDeck.removeTabAt(index);
            }
        });
        add(crossLabel, BorderLayout.EAST);
        trigger.addStateConsumer(this);
        setOpaque(false);
    }

    public void stateChanged(TriggerEvent<? extends String> event) {
        _titleLabel.setText(event.getState());
        _diagramDeck.contentChanged(_diagramPane);
    }
}
