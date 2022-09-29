/**
 * Node that holds a string.
 */
public class StringNode extends Node {

    private final String string;

    /**
     * Constructor.
     * @param string payload of node.
     */
    public StringNode(String string){
        this.string = string;
    }

    /**
     * Accessor for string member.
     * @return member string.
     */
    public String getString() {
        return this.string;
    }

    /**
     * Get string representation of node.
     * @return string representation of node.
     */
    @Override
    public String toString() {
        return "String: \"" + this.string + "\"";
    }
}
