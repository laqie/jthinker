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

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Utility class for choosing files for save and open.
 * 
 * @author iappel
 */
public class ThinkerFileChooser {
    /**
     * Extension-based {@link FileFilter}.
     */
    public static class SuffixFilter extends FileFilter {
        private final String _description,  _suffix,  _suffix2;

        public SuffixFilter(String description, String suffix) {
            _description = description;
            _suffix = suffix;
            _suffix2 = suffix;
        }

        public SuffixFilter(String description, String suffix, String suffix2) {
            _description = description;
            _suffix = suffix;
            _suffix2 = suffix2;
        }

        @Override
        public String getDescription() {
            return _description;
        }

        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }

            String path = pathname.getPath();

            return path.endsWith(_suffix) || path.endsWith(_suffix2);
        }

        public String getSuffix() {
            return _suffix;
        }
    }

    public static SuffixFilter JTHINKER_FILES =
            new SuffixFilter("jThinker files", ".jthinker");
    public static SuffixFilter JPEG_FILES =
            new SuffixFilter("JPEG files", ".jpeg", ".jpg");
    public static SuffixFilter PNG_FILES =
            new SuffixFilter("PNG files", ".png");
    public static SuffixFilter HTML_FILES =
            new SuffixFilter("HTML files", ".html");

    /**
     * Chooses a place to store a file with given suffixes.
     * 
     * @param filters suffix-based filters
     * @return file to write or null if save was cancelled or zero filters
     * were provided
     */
    public static File chooseSave(SuffixFilter... filters) {
        JFileChooser chooser = new JFileChooser();
        if (filters == null || filters.length == 0) {
            return null;
        }
        
        for (SuffixFilter filter : filters) {
            chooser.setFileFilter(filter);
        }
        
        chooser.setAcceptAllFileFilterUsed(false);
        int code = chooser.showSaveDialog(null);
        if (code != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        SuffixFilter current = (SuffixFilter) chooser.getFileFilter();
        File file = chooser.getSelectedFile();
        if (!current.accept(file)) {
            file = new File(file.getAbsolutePath() + current.getSuffix());
        }

        if (file.exists()) {
            String msg = "'" + file.getAbsolutePath() +
                    "' already exists.\n Should it be overwritten?";
            int option = JOptionPane.showConfirmDialog(null, msg,
                    "File already exists", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.NO_OPTION) {
                return null;
            }
        }
        
        return file;
    }

    /**
     * Chooses a place to restore a file with given suffixes.
     * 
     * @param filter suffix-based filter
     * @return file to read or null if appropriate file was not chosen
     */    
    public static File chooseLoad(SuffixFilter filter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        int code = chooser.showOpenDialog(null);
        if (code != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File f = chooser.getSelectedFile();
        if (!f.exists()) {
            return null;
        } else {
            return f;
        }
    }
}
