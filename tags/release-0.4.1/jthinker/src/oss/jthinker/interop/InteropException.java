package oss.jthinker.interop;

import java.io.IOException;
import java.net.URL;

/**
 * Interoperation exception.
 */
public class InteropException extends Exception {
    /**
     * Creates a new instance of InteropException.
     *
     * @param url URL, during access to which the problem has occured
     * @param cause I/O problem
     */
    public InteropException(String url, IOException cause) {
        super("Problem accessing " + url, cause);
    }

    /**
     * Creates a new instance of InteropException.
     *
     * @param message textual description of the problem
     */
    public InteropException(String message) {
        super(message);
    }

    public InteropException(URL url, IOException ex) {
        this(url.toString(), ex);
    }
}

