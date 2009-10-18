package oss.jthinkserver.storage;

import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Single log record on internal application functioning.
 *
 * @author iappel
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class LogEntry {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long _entryKey;

    @Persistent
    private LogEntryType _entryType;

    @Persistent
    private Date _entryDateTime;

    @Persistent
    private String _extraInfo;

    /**
     * Creates a new log record with given type and no extra info.
     *
     * @param type log record type
     */
    public LogEntry(LogEntryType type) {
        this(type, null);
    }

    /**
     * Creates a new log record with given type and extra info.
     *
     * @param type log record type
     * @param extra extra information
     */
    public LogEntry(LogEntryType type, String extra) {
        _entryType = type;
        _entryDateTime = new Date();
        _extraInfo = extra;
    } 
} 

