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
package oss.jthinker.views;

import java.awt.Component;
import java.net.URL;
import javax.swing.JMenuBar;

/**
 * Common interface for platform-specific functions.
 */
public interface EntryPoint {
    /**
     * Is local save/load supported on this platform?
     *
     * @return true if local save/load is supported,
     *         false otherwise (for example in applet environment)
     */
    boolean localPersistence();

    /**
     * Can this application be terminated?
     *
     * @return true if {@link System#exit(int)} will terminate
     *         the application properly, false otherwise
     */
    boolean isTerminatable(); 

    /**
     * Sets menu bar of the application view.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param menuBar menu bar to set
     */
    void setJMenuBar(JMenuBar menuBar);

    /**
     * Adds a widget to the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param component widget to add
     * @param param addition parameters
     */ 
    void add(Component component, Object param);

    /**
     * Removes a widget from the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame},
     * see that classes for details.
     *
     * @param component widget to remove 
     */
    void remove(Component component);

    /**
     * Validates the layout of the container.
     * This method is actually implemented in
     * {@link javax.swing.JApplet} and {@link javax.swing.JFrame),
     * see that classes for details.
     */
    void validate();

    /**
     * Opens the browser.
     *
     * @param url URL to open
     */
    void openBrowser(URL url);
}
