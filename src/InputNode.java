import java.util.ArrayList;

/**
 * Representation of a input node.
 */
public class InputNode extends StatementNode {

    private final ArrayList<Node> nodes;

    /**
     * Constructor
     * @param nodes as member list of nodes.
     */
    public InputNode(ArrayList<Node> nodes){
        this.nodes = new ArrayList<>(nodes);
    }

    /**
     * Get member nodes.
     * @return member nodes.
     */
    public ArrayList<Node> getNodes(){
        return this.nodes;
    }

    /**
     * Get string representation of nodes.
     * @return string representation of nodes.
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("Input: { ");
        for (Node node : nodes)
            returnString.append(node.toString()).append(", ");
        int len = returnString.length();
        return returnString.delete(len - 2, len).append(" }").toString();
    }
}
