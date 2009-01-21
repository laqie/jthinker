/*
 * Copyright (c) 2008, Ivan Appel <ivan.appel@gmail.com>
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

package oss.jthinker.diagrams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Simple {@link DiagramSpec} that is loads itself from XML file using
 * {@link XMLUtils} and keeps the file name.
 * 
 * @author iappel
 */
public class FileDiagramSpec extends DiagramSpec {
    public final File file;

    /**
     * Loads a diagram from XML file.
     * 
     * @param file file to get data from.
     * @throws SAXException on problems with file parsing.
     * @throws IOException on problems with file access.
     */
    public FileDiagramSpec(File file) throws SAXException, IOException {
        super(loadInstance(file));
        this.file = file;
    }
    
    /**
     * Loads a diagram specification from XML file.
     * 
     * @param f XML file
     * @return diagram specification
     * @throws SAXException on parsing errors
     * @throws IOException on I/O errors of loading a file
     */
    public static DiagramSpec loadInstance(File f) throws SAXException, IOException {
        FileInputStream stream = new FileInputStream(f);
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        char[] cData = new char[(int) f.length()];
        reader.read(cData);

        return parse(new String(cData));
    }

    /**
     * Loads a diagram specification from string buffer.
     * 
     * @param rawContent string buffer
     * @return diagram specification
     * @throws SAXException on parsing errors
     * @throws IOException on I/O errors of loading a file
     */
    public static DiagramSpec parse(String rawContent) throws SAXException, IOException {
        String content = rawContent.trim();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder parser = dbf.newDocumentBuilder();
            Reader reader = new StringReader(content);
            InputSource source = new InputSource(reader);
            Document doc = parser.parse(source);
            if (doc == null) {
                throw new NullPointerException();
            }
            return new DiagramSpec(doc.getFirstChild());
        } catch (ParserConfigurationException pex) {
            throw new RuntimeException("Unable to load data");
        }
    }
}
