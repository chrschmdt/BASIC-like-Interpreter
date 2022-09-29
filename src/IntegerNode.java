/**
 * Node that holds an integer.
 */
public class IntegerNode extends Node {

    private final Integer integer;

    /**
     * Constructor
     * @param integer integer to hold.
     */
    public IntegerNode(Integer integer){
        this.integer = integer;
    }

    /**
     * Getter for payload.
     * @return integer held.
     */
    public Integer getInteger() {
        return integer;
    }

    /**
     * Make a string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return "integer: \"" + integer.toString() + "\"";
    }
}
