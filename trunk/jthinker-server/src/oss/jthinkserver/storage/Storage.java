package oss.jthinkserver.storage;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * Singleton class for various data access functionality.
 *
 * @author iappel
 */
public class Storage {
    private static PersistenceManagerFactory _factory;
    private static PersistenceManager _manager;

    private synchronized static PersistenceManagerFactory getFactory() {
        if (_factory == null) {
            _factory = JDOHelper.getPersistenceManagerFactory("transactions-optional");
        }
        return _factory;
    }

    private synchronized static PersistenceManager getManager() {
        if (_manager == null) {
            _manager = getFactory().getPersistenceManager();
        }
        return _manager;
    }

    /**
     * Commits the pending changes.
     */
    public static void commit() {
        if (_manager != null) {
            _manager.close();
            _manager = null;
        }
    }

    /**
     * Logs a new access to /internal/version servlet.
     */
    public static void logVersionCheck() {
        LogEntry entry = new LogEntry(LogEntryType.versionCheck);
        getManager().makePersistent(entry);
    }
}

