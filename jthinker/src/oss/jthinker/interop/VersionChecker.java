package oss.jthinker.interop;

import java.util.List;

/**
 * Utility class for checking version of jThinker.
 */
public class VersionChecker {
    public static final String CURRENT_VERSION = "0.4.1";

    /**
     * Checks the version of application against the last
     * released version.
     *
     * @return false when application's version match the
     *         last released one or there was an error
     *         obtaining the version number from server
     */
    public static boolean checkVersion() {
        try {
            List<String> res = InteropUtils.request("/internal/version");
            if (res.isEmpty()) {
                return false;
            }
            String version = res.get(0);
            return !(CURRENT_VERSION.equals(version));
        } catch (InteropException iex) {
        }
        return false;
    }
}

