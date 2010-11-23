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

package oss.jthinker.datamodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import org.w3c.dom.Element;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import oss.jthinker.util.XMLStored;

/**
 * Container for packaging all information on diagram's structure
 * and data.
 * 
 * @author iappel
 */
public class DiagramData implements XMLStored, DiagramDataSource {
    private final List<JNodeData> _nodes = new ArrayList<JNodeData>();
    private final List<JEdgeData> _edges = new ArrayList<JEdgeData>();
    private final List<JLegData> _legs = new ArrayList<JLegData>();
    private final DiagramType _type;
    private final DiagramOptionData _options = new DiagramOptionData();
    private final File _file;

    /**
     * Creates a new empty DiagramSpec instance.
     * 
     * @param type diagram's type
     */
    public DiagramData(DiagramType type) {
        _type = type;
        _file = null;
    }

    /**
     * Creates a new DiagramSpec instance and loads information from
     * file.
     *
     * @param file file to load
     * @throws SAXException where there are problems parsing the file
     * @throws IOException when there are problems reading the file
     */
    public DiagramData(File file)
    throws SAXException, IOException, ParserConfigurationException {
        _file = file;
        DiagramDataSource data = XMLUtils.load(file);
        _type = data.getDiagramType();
        load(data);
    }

    public final void load(DiagramDataSource datasource) {
        _nodes.clear();
        _nodes.addAll(datasource.getNodeData());
        _edges.clear();
        _edges.addAll(datasource.getEdgeData());
        _legs.clear();
        _legs.addAll(datasource.getLegData());
        _options.fill(datasource.getOptions());
    }
    
    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof DiagramData) {
            DiagramData spec = (DiagramData)obj;
            return _nodes.equals(spec._nodes) &&
                   _edges.equals(spec._edges) &&
                   _legs.equals(spec._legs) &&
                   _type.equals(spec._type) &&
                   _options.equals(spec._options);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return _nodes.hashCode() + _edges.hashCode() +
               _legs.hashCode() + _type.hashCode() + _options.hashCode();
    }

    /** {@inheritDoc} */
    public Element saveToXML(Document document) {
        Element result = document.createElement("diagram");
        result.setAttribute("type", _type.toString());
        for (JNodeData spec : _nodes) {
            result.appendChild(spec.saveToXML(document));
        }
        for (JEdgeData spec : _edges) {
            result.appendChild(spec.saveToXML(document));
        }
        for (JLegData spec : _legs) {
            result.appendChild(spec.saveToXML(document));
        }
        result.appendChild(_options.saveToXML(document));
        return result;
    }

    public String renderXML() {
        StringWriter writer = new StringWriter();
        saveToStream(writer);
        return writer.toString();
    }

    /**
     * Saves a diagram specification into an XML file.
     * 
     * @param f file to use
     * @throws FileNotFoundException if the file exists but is a directory
     *                   rather than a regular file, does not exist but cannot
     *                   be created, or cannot be opened for any other reason
     * {@see FileOutputStream}
     */
    public void save(File f) throws FileNotFoundException {
        try {
            PrintWriter printer = new PrintWriter(f, "UTF-8");
            saveToStream(printer);
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(null, ex,
		    "Unable to save file due to internal problems",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
    }   

    private void saveToStream(Writer printer) {
        Document doc;
        Transformer writer;

        try {
            writer = TransformerFactory.newInstance().newTransformer();
            DocumentBuilder builder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();
            Element data = saveToXML(doc);
            doc.appendChild(data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex,
                    "Unable to save file due to internal problems",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(printer);

        writer.setOutputProperty(OutputKeys.INDENT, "yes");

        try {
            writer.transform(source, result);
        } catch (TransformerException ex) {
            JOptionPane.showMessageDialog(null, ex,
                    "Unable to save file due to internal problems",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public List<JNodeData> getNodeData() {
        return Collections.unmodifiableList(_nodes);
    }

    public List<JEdgeData> getEdgeData() {
        return Collections.unmodifiableList(_edges);
    }

    public List<JLegData> getLegData() {
        return Collections.unmodifiableList(_legs);
    }

    public DiagramOptionData getOptions() {
        return _options;
    }

    public DiagramType getDiagramType() {
        return _type;
    }

    public File getFile() {
        return _file;
    }
}
