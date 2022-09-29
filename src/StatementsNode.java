import java.util.ArrayList;

/**
 * Node that contains a list of statements.
 */
public class StatementsNode extends Node implements NodeWithStatements {

    private final ArrayList<StatementNode> nodes;

    /**
     * Constructor.
     */
    public StatementsNode(){
        nodes = new ArrayList<>();
    }

    /**
     * Add node to member list.
     * @param node to add.
     */
    public void addNode(StatementNode node){
        this.nodes.add(node);
    }

    /**
     * Accessor for node list.
     * @return node list.
     */
    public ArrayList<StatementNode> getStatements(){
        return nodes;
    }

    /**
     * Get string representation of node.
     * @return string representation of node.
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("Statements: { ");
        for (Node node : nodes)
            returnString.append(node.toString()).append(", ");
        int len = returnString.length();
        return returnString.delete(len - 2, len).append(" }").toString();
    }
}
