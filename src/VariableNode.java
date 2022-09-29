/**
 * Node that represents variable.
 */
public class VariableNode extends Node {

    private final String name;

    /**
     * Constructor
     * @param name of variable.
     */
    public VariableNode(String name){
        this.name = name;
    }

    /**
     * Accessor for name of variable.
     * @return name of variable.
     */
    public String getName() {
        return name;
    }

    /**
     * Get string representation of variable.
     * @return string representation of variable.
     */
    @Override
    public String toString() {
        return "Variable: \"" + this.name + "\"";
    }
}
