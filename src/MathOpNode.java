import java.util.HashMap;

/**
 * Node that represents a math operation.
 */
public class MathOpNode extends Node {

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
    }

    public static HashMap<TokenType, Operation> tokenOpMap = new HashMap<>();
    static {
        tokenOpMap.put(TokenType.ADD, Operation.ADD);
        tokenOpMap.put(TokenType.SUBTRACT, Operation.SUBTRACT);
        tokenOpMap.put(TokenType.MULTIPLY, Operation.MULTIPLY);
        tokenOpMap.put(TokenType.DIVIDE, Operation.DIVIDE);
    }

    private final Operation operation;
    private final Node left;
    private final Node right;

    /**
     * Constructor.
     * @param operation operation type of node.
     * @param left operand.
     * @param right operand.
     */
    public MathOpNode(Operation operation, Node left, Node right){
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    /**
     * Getter for left side of operation.
     * @return left side of operation.
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Getter for right side of operation.
     * @return right side of operation.
     */
    public Node getRight() {
        return right;
    }

    /**
     * Getter for operator.
     * @return operator.
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Make a string representation of the node.
     * @return string representation of the node.
     */
    @Override
    public String toString() {
        return  "Operation: { left: \""
                + left + "\", right: \""
                + right + "\", operator: \""
                + operation + "\" }";
    }
}
