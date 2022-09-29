import java.util.HashMap;

public class BooleanOperationNode extends Node {

    // publicly define operation types
    public enum Operator {
        EQUALS,
        NOTEQUALS,
        GT,
        GTE,
        LT,
        LTE
    }

    // allow match from TokenType to local enum
    public static HashMap<TokenType, Operator> tokenOpMap = new HashMap<>();
    static {
        tokenOpMap.put(TokenType.EQUALS, Operator.EQUALS);
        tokenOpMap.put(TokenType.NOTEQUALS, Operator.NOTEQUALS);
        tokenOpMap.put(TokenType.GT, Operator.GT);
        tokenOpMap.put(TokenType.GTE, Operator.GTE);
        tokenOpMap.put(TokenType.LT, Operator.LT);
        tokenOpMap.put(TokenType.LTE, Operator.LTE);
    }

    private Node left;
    private Node right;
    private final Operator operator;

    public BooleanOperationNode(Node left, Node right, Operator operator){
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    /**
     * Get left side of operation.
     * @return left side of operation.
     */
    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    /**
     * Get right side of operation.
     * @return right side of operation.
     */
    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    /**
     * Get operator of operation.
     * @return operator of operation.
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Get string representation of node.
     * @return string representation of node.
     */
    @Override
    public String toString() {
        return "BooleanOperation: {" +
                "left: \"" + left +
                "\" , right: \"" + right +
                "\" , operation: \"" + operator +
                "\" }";
    }
}
