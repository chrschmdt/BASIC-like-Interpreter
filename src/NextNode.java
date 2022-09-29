/**
 * Node for the next command of the for loop.
 */
public class NextNode extends StatementNode {

    private final VariableNode var;

    /**
     * Constructor
     * @param var var of for loop to be exited.
     */
    public NextNode(VariableNode var){
        this.var = var;
    }

    /**
     * Getter for VariableNode.
     * @return VariableNode.
     */
    public VariableNode getVar() {
        return var;
    }

    /**
     * Get string representation of nodes.
     * @return string representation of nodes.
     */
    @Override
    public String toString() {
        return "Next: \"" + var + "\"";
    }
}
