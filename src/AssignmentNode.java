/**
 * Node that represents an assignment operation.
 */
public class AssignmentNode extends StatementNode {

    private final VariableNode variable;
    private final Node value;

    /**
     * Constructor.
     * @param variable that will receive value.
     * @param value that will be assigned.
     */
    public AssignmentNode(VariableNode variable, Node value){
        this.variable = variable;
        this.value = value;
    }

    /**
     * Getter for assignment variable.
     * @return assignment variable.
     */
    public VariableNode getVariable() {
        return this.variable;
    }

    /**
     * Getter for assignment value.
     * @return assignment value.
     */
    public Node getValue() {
        return this.value;
    }

    /**
     * Get string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return "Assignment: { from: \""
                + this.value
                + "\", to: \""
                + this.variable
                + "\" }";
    }
}
