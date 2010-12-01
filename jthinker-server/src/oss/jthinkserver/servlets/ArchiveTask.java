package oss.jthinkserver.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oss.jthinkserver.storage.Storage;

/**
 *
 * @author iappel
 */
public class ArchiveTask extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("OK");

        Storage.archiveVersionCheck();
        Storage.commit();
    }
}
