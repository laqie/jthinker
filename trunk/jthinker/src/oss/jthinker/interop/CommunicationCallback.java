package oss.jthinker.interop;

import java.net.URL;

public interface CommunicationCallback {
    void openInBrowser(URL url);

    void reportFailure(InteropException ex);
}

