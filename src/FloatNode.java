/**
 * Node that holds a float.
 */
public class FloatNode extends Node{

    private final Float fl;

    /**
     * Constructor
     * @param fl float to hold.
     */
    public FloatNode (Float fl){
        this.fl = fl;
    }

    /**
     * Getter for payload.
     * @return float held.
     */
    public Float getFloat() {
        return fl;
    }

    /**
     * Make a string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return "float: \"" + fl.toString() + "\"";
    }
}
