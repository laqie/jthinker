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
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import org.w3c.dom.Element;
import oss.jthinker.tocmodel.DiagramType;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import oss.jthinker.util.XMLStored;
import oss.jthinker.widgets.JNodeSpec;

/**
 * Container for packaging all information on diagram's structure
 * and data.
 * 
 * @author iappel
 */
public class DiagramSpec implements XMLStored {
    public final List<JEdgeSpec> edgeSpecs;
    public final List<JNodeSpec> nodeSpecs;
    public final List<JLegSpec> legSpecs;
    public final DiagramType type;
    public final DiagramOptionSpec options = new DiagramOptionSpec();

    protected DiagramSpec(Node data) {
        if (!data.getNodeName().equals("diagram")) {
            throw new IllegalArgumentException(data.getNodeName());
        }
        Node typeNode = data.getAttributes().getNamedItem("type");
        if (typeNode == null) {
            throw new IllegalArgumentException("Diagram type missing");
        }
        String typeStr = typeNode.getNodeValue();
        type = DiagramType.valueOf(typeStr);
        edgeSpecs = new LinkedList<JEdgeSpec>();
        nodeSpecs = new LinkedList<JNodeSpec>();
        legSpecs = new LinkedList<JLegSpec>();

        NodeList content = data.getChildNodes();

        DiagramOptionSpec tempOptions = null;
        
        for (int i = 0; i < content.getLength(); i++) {
            Node n = content.item(i);
            String name = n.getNodeName();
            if (name.equals("node")) {
                JNodeSpec spec = JNodeSpec.loadInstance(n);
                nodeSpecs.add(spec);
            } else if (name.equals("edge")) {
                JEdgeSpec spec = new JEdgeSpec(n);
                edgeSpecs.add(spec);
            } else if (name.equals("leg")) {
                JLegSpec spec = new JLegSpec(n);
                boolean t = legSpecs.add(spec);
            } else if (name.equals("options")) {
                options.fill(new DiagramOptionSpec(n));
            }
        }
    }

    /**
     * Creates a new DiagramSpec instance.
     * 
     * @param nodeSpecs list of diagram's nodes
     * @param edgeSpecs list of diagram's edges
     * @param legSpecs list of diagram's legs
     * @param type type of the diagram
     */    
    public DiagramSpec(List<JNodeSpec> nodeSpecs, List<JEdgeSpec> edgeSpecs,
            List<JLegSpec> legSpecs, DiagramType type) {
        this.edgeSpecs = edgeSpecs != null ? edgeSpecs :
            new LinkedList<JEdgeSpec>();
        this.nodeSpecs = nodeSpecs != null ? nodeSpecs :
            new LinkedList<JNodeSpec>();
        this.legSpecs = legSpecs != null ? legSpecs :
            new LinkedList<JLegSpec>();
        this.type = type;
    }

    /**
     * Copies a DiagramSpec from another instance.
     * 
     * @param spec specificatio to copy
     */
    public DiagramSpec(DiagramSpec spec) {
        this(spec.nodeSpecs, spec.edgeSpecs, spec.legSpecs, spec.type);
    }
    
    /**
     * Creates a new empty DiagramSpec instance.
     * 
     * @param type diagram's type
     */
    public DiagramSpec(DiagramType type) {
        this(null, null, null, type);
    }
    
    @Override
    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (obj instanceof DiagramSpec) {
            DiagramSpec spec = (DiagramSpec)obj;
            return nodeSpecs.equals(spec.nodeSpecs) &&
                    edgeSpecs.equals(spec.edgeSpecs) &&
                    legSpecs.equals(spec.legSpecs) &&
                    type.equals(spec.type) &&
                    options.equals(spec.options);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    /** {@inheritDoc} */
    public int hashCode() {
        return nodeSpecs.hashCode() + edgeSpecs.hashCode() +
                legSpecs.hashCode() + type.hashCode() + options.hashCode();
    }

    /** {@inheritDoc} */
    public Element saveToXML(Document document) {
        Element result = document.createElement("diagram");
        result.setAttribute("type", type.toString());
        for (JNodeSpec spec : nodeSpecs) {
            result.appendChild(spec.saveToXML(document));
        }
        for (JEdgeSpec spec : edgeSpecs){
            result.appendChild(spec.saveToXML(document));
        }
        for (JLegSpec spec : legSpecs){
            result.appendChild(spec.saveToXML(document));
        }
        result.appendChild(options.saveToXML(document));
        return result;
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
        DocumentBuilder builder;
        Transformer writer;
        PrintStream printer;

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            writer = TransformerFactory.newInstance().newTransformer();
            printer = new PrintStream(f, "UTF-8");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex,
                    "Unable to save file due to internal problems",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Document doc = builder.newDocument();
        Element data = saveToXML(doc);
        doc.appendChild(data);
        
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
}
