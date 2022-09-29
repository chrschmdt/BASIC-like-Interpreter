import java.util.ArrayList;

/**
 * Node for a function.
 */
public class FunctionNode extends StatementNode {

    // publicly define which tokens are functions
    public final static TokenType[] fnTokens = {
            TokenType.FN_RANDOM,
            TokenType.FN_LEFT,
            TokenType.FN_RIGHT,
            TokenType.FN_MID,
            TokenType.FN_NUM,
            TokenType.FN_VAL,
            TokenType.FN_VAL_F
    };

    private final ArrayList<Node> params;
    private final TokenType function;

    /**
     * Constructor.
     * @param params function parameters.
     * @param function type of function.
     */
    public FunctionNode(ArrayList<Node> params, TokenType function){
        this.params = params;
        this.function = function;
    }

    /**
     * Get parameters of function.
     * @return parameters of function.
     */
    public ArrayList<Node> getParams() {
        return params;
    }

    /**
     * Get type of function.
     * @return type of function.
     */
    public TokenType getFunction() {
        return function;
    }

    /**
     * Get string representation of node.
     * @return string representation of node.
     */
    @Override
    public String toString() {
        String paramsString = "params: {}";
        if (params.size() > 0) {
            StringBuilder returnString = new StringBuilder("params: { ");
            for (Node node : params)
                returnString.append(node.toString()).append(", ");
            int len = returnString.length();
            paramsString = returnString.delete(len - 2, len).append(" }").toString();
        }
        return "Function: { " +
                "function: \"" + function.name() +
                "\", " + paramsString + " }";
    }
}
