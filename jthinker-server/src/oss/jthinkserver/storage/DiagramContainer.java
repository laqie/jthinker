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

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Text;

import java.util.Random;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Abstract base class for persistant saving of diagrams. 
 *
 * @author iappel
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public abstract class DiagramContainer {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long _primaryKey;

    @Persistent
    private String _secretId;

    @Persistent
    private Text _htmlContent;

    @Persistent
    private Text _xmlContent; 

    @Persistent
    private Blob _pngContent;

    /**
     * Creates a new log record with given type and no extra info.
     *
     * @param type log record type
     */
    protected DiagramContainer() { 
    }

    /**
     * Returns the "secret" ID of the object.
     * 
     * "Secret ID" is a primary key with several random digits
     * appended, so it's virtually impossible to guess, is an
     * object with this ID exists or not.
     */
    public final String getSecretId() {
        if (_secretId == null) {
            throw new IllegalStateException("Secret ID not set");
        }
        return _secretId;
    }

    /**
     * Generates the "secret ID". Primary key must be set
     * at the time of method's invocation.
     *
     * @throws IllegalStateException if primary key is not set yet
     */
    protected final void generateSecretId() {
        if (_primaryKey == null) {
            throw new IllegalStateException("Primary key not set");
        }
        String index = _primaryKey.toString();
        Random random = new Random();
        index = index + random.nextInt(1000000);
        _secretId = index;
    }

    /**
     * Returns the URL for the diagram object.
     */
    public abstract String getDisplayPage();

    /**
     * Sets the HTML content of diagram.
     *
     * @param htmlData HTML data
     */
    public final void setHtmlContent(String htmlData) {
        // TODO: Add validation
        _htmlContent = new Text(htmlData);
    }

    /**
     * Sets the XML content of diagram.
     *
     * @param xmlData XML data
     */
    public final void setXmlContent(String xmlData) {
        _xmlContent = new Text(xmlData);
    }

    /**
     * Sets the PNG content of diagram.
     *
     * @param rawData PNG image as a byte array
     */
    public final void setPngContent(byte[] rawData) {
        _pngContent = new Blob(rawData);
    }

    /**
     * Returns the XML content of the diagram.
     */
    public final String getXmlContent() {
        if (_xmlContent == null) {
            return null;
        }
        return _xmlContent.getValue();
    }

    /**
     * Returns the PNG content of the diagram as
     * a byte array.
     */
    public final byte[] getPngContent() {
        if (_pngContent == null) {
            return null;
        }
        return _pngContent.getBytes();
    } 
} 

