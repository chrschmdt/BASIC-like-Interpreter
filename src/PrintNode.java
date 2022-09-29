import java.util.ArrayList;

/**
 * Representation of a print node.
 */
public class PrintNode extends StatementNode {

    private final ArrayList<Node> nodes;

    /**
     * Constructor
     * @param nodes as member list of nodes.
     */
    public PrintNode(ArrayList<Node> nodes){
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
        StringBuilder returnString = new StringBuilder("Print: { ");
        for (Node node : nodes)
            returnString.append(node.toString()).append(", ");
        int len = returnString.length();
        return returnString.delete(len - 2, len).append(" }").toString();
    }
}
