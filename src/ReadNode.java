import java.util.ArrayList;

/**
 * Representation of a read node.
 */
public class ReadNode extends StatementNode {

    private final ArrayList<VariableNode> nodes;

    /**
     * Constructor
     * @param nodes as member list of nodes.
     */
    public ReadNode(ArrayList<VariableNode> nodes){
        this.nodes = new ArrayList<>(nodes);
    }

    /**
     * Get member nodes.
     * @return member nodes.
     */
    public ArrayList<VariableNode> getNodes(){
        return this.nodes;
    }

    /**
     * Get string representation of nodes.
     * @return string representation of nodes.
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("Read: { ");
        for (Node node : nodes)
            returnString.append(node.toString()).append(", ");
        int len = returnString.length();
        return returnString.delete(len - 2, len).append(" }").toString();
    }
}
