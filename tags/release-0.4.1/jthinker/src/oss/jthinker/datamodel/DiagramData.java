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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.w3c.dom.Element;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

    public void save(File f) throws FileNotFoundException {
        XMLUtils.save(this, f);
    }

    public String renderXML() {
        return XMLUtils.renderXML(this);
    }
}
