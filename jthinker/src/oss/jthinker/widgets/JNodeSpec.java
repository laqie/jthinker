package oss.jthinker.widgets;

import java.awt.Point;

/**
 * A {@link JNode} construction specification.
 * 
 * @author iappel
 */
public class JNodeSpec extends JSlideSpec {
    private final boolean editable;
    private final String content;

    /**
     * Creates a new JNodeSpec instance.
     * 
     * @param borderType type of border around the node
     * @param editable defines, is node's content editable or not
     * @param content string content of the node
     * @param center desired center point for the slide
     */
    public JNodeSpec(BorderType borderType, boolean editable, String content,
            Point center) {
        super(center, borderType);
        this.editable = editable;
        this.content = content;
    }

    /**
     * Returns desired content for the node.
     * 
     * @return desired content for the node.
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns, should node be editable or not.
     * 
     * @return desired modifyability of the node.
     */
    public boolean isEditable() {
        return editable;
    }
    
    /**
     * Returns a component to put into the node.
     * 
     * @return a component to put into the node.
     */
    protected JLabelBundle createComponent() {
        return new JLabelBundle(content);
    }
    
    /**
     * Creates a new JNodeSpec with the same border type and editability,
     * but with other content and placement.
     * 
     * @param otherContent content for new node
     * @param otherCenter place for new node
     * @return newly cloned JNodeSpec instance
     */
    public JNodeSpec clone(String otherContent, Point otherCenter) {
        BorderType borderType = super.getBorderType();
        return new JNodeSpec(borderType, editable, otherContent, otherCenter);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        BorderType borderType = super.getBorderType();
        sb.append(borderType.name() + "." + borderType.ordinal() + ".");
        sb.append(editable + ".");
        Point p = getSlideCenter();
        sb.append(p.x + "." + p.y + " $ ");
        sb.append(content);
        return sb.toString();
    }
}
