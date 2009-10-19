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

package oss.jthinker.interop;

import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO: Write spec 
 * 
 * @author iappel
 */
public class DiagramPublisher {
    private String _xmlData;
    private String _htmlData;
    private byte[] _pngData;
    private CommunicationCallback _callback;

    public void setXMLData(String doc) {
        _xmlData = doc;
    }

    public void setImageData(byte[] img) {
        _pngData = img;
    }

    public void setHTMLData(String wrapper) {
        _htmlData = wrapper;
    }

    public void setCallback(CommunicationCallback callback) {
        _callback = callback;
    }

    public synchronized void publish() {
        if (_callback == null) {
            return;
        }
        PublicationThread thread = new PublicationThread(
            _xmlData, _htmlData, _pngData, _callback
        );
        thread.start();
    }

    private static class PublicationThread extends Thread {
        private final byte[] _xmlData;
        private final byte[] _htmlData;
        private final byte[] _pngData;
        private final CommunicationCallback _callback;

        private URL _browserURL;
        private String _sessionId;

        public PublicationThread(String xml, String html, byte[] png,
                                 CommunicationCallback callback) {
            _xmlData = xml.getBytes();
            _htmlData = html.getBytes();
            _pngData = png;
            _callback = callback;
        }

        public void run() {
            try {
                handshake();
                if (_xmlData != null) {
                    transmit(_xmlData, "text/xml");
                }
                if (_htmlData != null) {
                    transmit(_htmlData, "text/html");
                }
                if (_pngData != null) {
                    transmit(_pngData, "image/png");
                }
            } catch (InteropException ex) {
                _callback.reportFailure(ex);
                return;
            }
            _callback.openInBrowser(_browserURL);
        }

        private void handshake() throws InteropException {
            List<String> response = InteropUtils.request("/handshake");
            _sessionId = null;
            _browserURL = null;
            for (String line : response) {
                if (line.startsWith("Session")) {
                    _sessionId = line.split("=")[1].trim();
                } else if (line.startsWith("Result-page")) {
                    try {
                        _browserURL = new URL(line.split("=")[1].trim());
                    } catch (MalformedURLException ex) {
                        throw new InteropException("Error during handshake", ex);
                    }
                }
            }
            throw new InteropException("Wrong response from service" + response);
        }

        private void transmit(byte[] data, String contentType)
        throws InteropException {
            InteropUtils.doHttpPost("/upload", data, contentType, "session = " + _sessionId);
        }            
    }
}
