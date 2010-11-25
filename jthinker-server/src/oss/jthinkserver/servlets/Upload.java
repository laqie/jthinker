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

package oss.jthinkserver.servlets;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 

import oss.jthinkserver.storage.Storage;

/**
 * Handshake servlet for obtaining the security session
 * information.
 *
 */
public class Upload extends HttpServlet {
    private final static Logger logger = Logger.getLogger(Upload.class.getName());

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
        String sessionId = null;

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("Session")) {
                sessionId = cookie.getValue();
            }
        }

        logger.log(Level.WARNING, "SessionId = {0}", sessionId);

        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID not set");
        }

        String contentType = req.getContentType();
        int contentSize = req.getContentLength();
        byte[] rawData = new byte[contentSize];
        int offset = 0;
        do { 
            offset += req.getInputStream().read(rawData, offset, contentSize); 
        } while (offset < contentSize);

        logger.log(Level.WARNING, "Content is {0}", contentType);

        if (contentType.equals("text/html")) {
            String htmlData = new String(rawData);
            Storage.setSessionHtml(sessionId, htmlData);
        } else if (contentType.equals("text/xml")) {
            String xmlData = new String(rawData);
            Storage.setSessionXml(sessionId, xmlData);
        } else if (contentType.equals("image/png")) {
            Storage.setSessionPng(sessionId, rawData);
        } else {
            throw new IllegalArgumentException("Invalid content type: " + contentType);
        }

        resp.setContentType("text/plain");
        resp.getWriter().write("Success");
        Storage.commit();
    }
}

