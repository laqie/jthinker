package oss.jthinker.widgets;

/**
 * Interface of a {@link JNode} hoster that manages some of the linking
 * and moving issues.
 * 
 * @author iappel
 */
public interface JNodeHost {
    /**
     * Should be called when some of hosted {@link JNode}s were moved to
     * do some after-move actions.
     * 
     * @param node node that recently moved
     */    
    void dispatchJNodeMove(JNode node);
    
    /**
     * Starts selecting peer to link with given node.
     * @param start node that will be a start for edge after linking
     */
    void startLinking(JNode start);
    
    /**
     * Selects a peer for linking with the node, that was chosen
     * via {@link startLinking(JNode)} method.
     * @param end node that should be the end of the new edge
     */
    void endLinking(JNode end);
    
    /**
     * Deletes given node.
     * @param node node to delete
     */
    void deleteJNode(JNode node);
    
    /**
     * Starts editing of selected node.
     * @param node node to edit
     */
    void editJNodeContent(JNode node);
    
    /**
     * Enables or disables numbering of nodes.
     * 
     * @param state should numbering be enabled or disabled
     */
    void enableNodeNumbering(boolean state);

    /**
     * Returns a numeric index of the node.
     * 
     * @param node node to seek
     * @return node's numeric index
     */
    int issueIndex(JNode node);
    
    /**
     * Returns a node-group associations holder.
     * 
     * @return grouping associations holder.
     */
    GroupHandler getGroupHandler();
  
    /**
     * Creates a new node and adds it to the manager and to the managed
     * view.
     * 
     * @param nodeSpec specification of the node to create
     * @return newly created node
     */
    JNode add(JNodeSpec nodeSpec);
    
    /**
     * Removes several nodes from the manager and from the managed view.
     * 
     * @param nodes nodes to remove
     */
    void remove(JNode... nodes);
}
