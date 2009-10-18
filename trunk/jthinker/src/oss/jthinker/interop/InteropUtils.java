package oss.jthinker.interop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for accessing the remote server.
 * 
 * @author iappel
 */
public class InteropUtils {
    private static String _hostName;

    /**
     * Requests data from the remote server.
     * Makes an HTTP GET request and returns the
     * result as a list of lines.
     * <br />
     * Usage of this method implies that accessed
     * data is a plain-text, rather than XML.
     *
     * @param urlName URL to fetch
     * @return fetch result as a list of lines
     */
    public static List<String> request(String urlName)
    throws InteropException {
        String fullUrl = getHostName() + urlName;

        InputStream istream;
        try {
            URL url = new URL(fullUrl);
            istream = url.openStream();
        } catch (MalformedURLException muex) {
            throw new InteropException(fullUrl, muex);
        } catch (IOException ioex) {
            throw new InteropException(fullUrl, ioex);
        }

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(istream)
        );
        List<String> result = new LinkedList<String>();
        try {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    return result;
                } else {
                    result.add(line);
                }
            }
        } catch (IOException ioex) {
            throw new InteropException(fullUrl, ioex);
        }
    }

    protected static String getHostName() {
        if (_hostName == null) {
            _hostName = "http://jthinker-server.appspot.com";
        }
        return _hostName;
    } 
}

         
