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
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import oss.jthinkserver.storage.Storage;
import oss.jthinkserver.storage.UploadSession;

/**
 * Handshake servlet for obtaining the security session
 * information.
 *
 * @author iappel
 */
public class NewChart extends HttpServlet {
    private final static Logger logger = Logger.getLogger(Handshake.class.getName());

    private String fetchParams(HttpServletRequest req, String key) {
        Map<String, String[]> params;
        params = (Map<String, String[]>)req.getParameterMap();
        String[] sessionIdArray = params.get(key); 
        if ((sessionIdArray != null) && (sessionIdArray.length > 0)) {
            return sessionIdArray[0];
        }
        return null;
    }

    private String fetchCookie(HttpServletRequest req, String key) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        } 
        return null;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
        UserService service = UserServiceFactory.getUserService();
        User user = service.getCurrentUser();
        String sessionId = fetchParams(req, "session");

        if (user == null) {
            if (sessionId == null) {
                throw new IllegalArgumentException("Session ID not set");
            }
            Cookie cookie = new Cookie("session", sessionId);
            cookie.setMaxAge(6000);
            resp.addCookie(cookie);
            resp.sendRedirect(service.createLoginURL("/newchart"));
            return;
        }

        if (sessionId == null) {
            // Probably, we had a login redirect
            sessionId = fetchCookie(req, "session");
            if (sessionId == null) {
                throw new IllegalArgumentException("Session ID not set");
            }
            Cookie cookie = new Cookie("session", "invalid");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            resp.sendRedirect("/newchart.jsp?session="+sessionId);
            return;
        }

        UploadSession session = Storage.lookupUploadSession(sessionId);
        String type = fetchParams(req, "type");
 
        if ("image".equals(type)) {
            resp.setContentType("image/png");
            OutputStream stream = resp.getOutputStream();
            byte[] content = session.getPngContent();
            if (content == null) {
                logger.log(Level.WARNING, "Content = null");
            } else {
                logger.log(Level.WARNING, "Content size = " + content.length);
            }
            stream.write(session.getPngContent());
            stream.flush();
            stream.close();
        } else { 
            if (type != null) {
                logger.log(Level.WARNING, "Unknown request type : {0}", type);
            }
            resp.sendRedirect("/newchart.jsp?session="+sessionId);
        } 
    }
}

