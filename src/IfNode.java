/**
 * Node for an if statement.
 */
public class IfNode extends StatementNode {

    private final BooleanOperationNode condition;
    private final VariableNode label;

    /**
     * Constructor.
     * @param condition boolean condition to meet.
     * @param label to go to if true.
     */
    public IfNode(BooleanOperationNode condition, VariableNode label){
        this.condition = condition;
        this.label = label;
    }

    /**
     * Get condition of node.
     * @return condition of node.
     */
    public BooleanOperationNode getCondition() {
        return condition;
    }

    /**
     * Get label of node.
     * @return label of node.
     */
    public VariableNode getLabel() {
        return label;
    }

    /**
     * Get string representation of node.
     * @return string representation of node.
     */
    @Override
    public String toString() {
        return "If: { " +
                "condition: \"" + condition +
                "\", label: \"" + label +
                "\" }";
    }
}
