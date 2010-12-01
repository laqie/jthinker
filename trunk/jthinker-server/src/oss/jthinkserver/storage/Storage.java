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
package oss.jthinkserver.storage;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

/**
 * Singleton class for various data access functionality.
 *
 * @author iappel
 */
public class Storage {
    private static final Logger logger = Logger.getLogger(Storage.class.getName());
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

    private static Query createQuery(Class cls) {
        return getManager().newQuery(cls);
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

    public static UploadSession createUploadSession() {
        UploadSession session = new UploadSession();
        getManager().makePersistent(session);
        session.generateSecretId();
        return session;
    }

    public static UploadSession lookupUploadSession(String sessionId) {
        Query q = createQuery(UploadSession.class);
        q.setFilter("_sessionId == sessionIndex");
        q.declareParameters("String sessionIndex");
        
        try {
            List<UploadSession> list = (List<UploadSession>)q.execute(sessionId);
            if (list.size() == 0) {
                logger.log(Level.WARNING, "Session lookup failed");
                return null;
            } else if (list.size() > 1) {
                logger.log(Level.WARNING, "Multiple sessions found");
            }
            return list.get(0);
        } finally {
            q.closeAll();
        }
    }

    public static void setSessionHtml(String sessionId, String htmlData) {
        UploadSession sess = lookupUploadSession(sessionId);
        if (sess == null) {
            throw new NullPointerException();
        }
        sess.setHtmlContent(htmlData);
    }
        
    public static void setSessionXml(String sessionId, String xmlData) {
        UploadSession sess = lookupUploadSession(sessionId);
        if (sess == null) {
            throw new NullPointerException();
        }
        sess.setXmlContent(xmlData);
    }

    public static void setSessionPng(String sessionId, byte[] rawData) {
        UploadSession sess = lookupUploadSession(sessionId);
        if (sess == null) {
            throw new NullPointerException();
        }
        sess.setPngContent(rawData);
    }

    public static void archiveVersionCheck() {
        Query entryQuery = createQuery(LogEntry.class);
        entryQuery.setFilter("_entryType == entryTypeArg");
        entryQuery.declareParameters("LogEntryType entryTypeArg");
        Query summaryQuery = null;

        try {
            List<LogEntry> entries = (List<LogEntry>)entryQuery.execute(LogEntryType.versionCheck);
            if (entries.isEmpty()) return;
            LogEntry entry = entries.get(0);

            Date entryDateTime = entry.getEntryDateTime();

            Calendar cal = Calendar.getInstance();
            cal.setTime(entryDateTime);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date entryDate = cal.getTime();

            summaryQuery = createQuery(LogEntry.class);
            summaryQuery.setFilter("_entryType == entryTypeArg && _entryDateTime == entryDateArg");
            summaryQuery.declareParameters("oss.jthinkserver.storage.LogEntryType entryTypeArg, java.util.Date entryDateArg");
            List<LogEntry> summaries = (List<LogEntry>)summaryQuery.execute(LogEntryType.versionCheckDaily, entryDate);
            if (summaries.isEmpty()) {
                LogEntry summary = new LogEntry(LogEntryType.versionCheckDaily, "1", entryDate);
                getManager().makePersistent(summary);
            } else {
                LogEntry summary = summaries.get(0);
                summary.setExtraData(summary.getExtraDataAsInt() + 1);
            }

            getManager().deletePersistent(entry);
        } finally {
            entryQuery.closeAll();

            if (summaryQuery != null) {
                summaryQuery.closeAll();
            }
        }
    }
}

