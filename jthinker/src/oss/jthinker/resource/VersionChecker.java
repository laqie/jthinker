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

package oss.jthinker.resource;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Simple class that checks jThinker's downloads RSS feed and
 * shows a dialog when version is updated.
 * 
 * @author iappel
 */
public class VersionChecker {
    public static final String CURRENT_VERSION = "0.4.0";
    private static final String FEED_URL =
            "http://code.google.com/feeds/p/jthinker/downloads/basic";
    private static final String WINDOWS_CMD =
            "rundll32 url.dll,FileProtocolHandler http://code.google.com/p/jthinker";
    
    private VersionChecker() {
    }
    
    /**
     * Checks version of the product, shows a message when new
     * version is available.
     */
    public static void checkVersion() {
        String version = getVersion();
        if (version==null || version.equals(CURRENT_VERSION)) {
            return;
        }
        
        if (System.getProperty("os.name").startsWith("Windows")) {
            int code = JOptionPane.showConfirmDialog(null,
                     "New version is available for download. Visit the site?",
                     "jThinker " + version + " is available",
                     JOptionPane.YES_NO_OPTION);
            if (code == JOptionPane.YES_OPTION) {
                try {
                    Runtime.getRuntime().exec(WINDOWS_CMD);
                } catch (Throwable t) {
                    JOptionPane.showMessageDialog(null,
                            "Sorry, unable to run web browser",
                            "Unable to run browser",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "New version is available for download. Visit the site!",
                    "jThinker " + version + " is available",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private static String getVersion() {
        try {
            return getVersion(FEED_URL);
        } catch (UnknownHostException ex) {
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    
    private static String getVersion(String url) throws Throwable {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = dbf.newDocumentBuilder();
        InputSource source = new InputSource(url);
        Document doc = parser.parse(source);
        NodeList entries = doc.getElementsByTagName("entry");
        ArrayList<String> contents = new ArrayList<String>();
        for (int i=0;i<entries.getLength();i++) {
            Node entry = entries.item(i);
            NodeList entryContent = entry.getChildNodes();
            for (int j=0;j<entryContent.getLength();j++) {
                Node entryItem = entryContent.item(j);
                if (entryItem.getNodeName().equals("title")) {
                    contents.add(entryItem.getFirstChild().getNodeValue());
                }
            }
        }
        return Collections.max(contents, new VersionComparator());
    }
    
    private static class VersionComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            String[] ver1 = o1.split(".");
            String[] ver2 = o2.split(".");
            for (int i=0;i<2;i++) {
                int result = ver1[i].compareTo(ver2[i]);
                if (result != 0) {
                    return result;
                }
            }
            return ver1[2].compareTo(ver2[2]);
        }
    }
}
