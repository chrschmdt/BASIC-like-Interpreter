/**
 * Node that holds a label to go to.
 */
public class GoSubNode extends StatementNode {

    private final String label;

    /**
     * Constructor.
     * @param label to go to.
     */
    public GoSubNode(String label){
        this.label = label;
    }

    /**
     * Getter for label.
     * @return label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Make a string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return "GoSub: \"" + label + '\"';
    }
}
