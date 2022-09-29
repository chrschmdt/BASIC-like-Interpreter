/**
 * A statement node with a label.
 */
public class LabeledStatementNode extends StatementNode{

    private final String label;
    private final StatementNode node;

    /**
     * Constructor.
     * @param label node's label.
     * @param node StatementNode with the label.
     */
    public LabeledStatementNode(String label, StatementNode node){
        this.label = label;
        this.node = node;
    }

    /**
     * Getter for label.
     * @return label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Getter for node.
     * @return node.
     */
    public StatementNode getNode() {
        return node;
    }

    /**
     * Make a string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return "LabeledStatement: { label: \"" +
                this.label + "\", " +
                this.node.toString() + " }";
    }
}
