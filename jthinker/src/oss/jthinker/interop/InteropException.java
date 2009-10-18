package oss.jthinker.interop;

import java.io.IOException;

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
}

