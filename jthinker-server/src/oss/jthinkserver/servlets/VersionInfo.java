package oss.jthinkserver.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import oss.jthinkserver.storage.Storage;

/**
 * A trivial servlet that outputs the current released
 * version of jThinker.
 */
public class VersionInfo extends HttpServlet {
    protected String VERSION_INFO = "0.4.0";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(VERSION_INFO);

        Storage.logVersionCheck();
        Storage.commit();
    }
}

