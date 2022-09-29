import java.util.ArrayList;

/**
 * Node for the for loop.
 */
public class ForNode extends StatementNode implements NodeWithStatements {

    private final VariableNode var;
    private final float start;
    private final float stop;
    private final float step;
    private final StatementsNode statements;
    private final NextNode nextNode;

    /**
     * Constructor
     * @param var for variable name.
     * @param start init var value.
     * @param stop end var value.
     * @param step increment amount.
     * @param statements statements to be executed.
     */
    public ForNode(
            VariableNode var,
            float start,
            float stop,
            float step,
            StatementsNode statements,
            NextNode nextNode
    ){
        this.var = var;
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.statements = statements;
        this.nextNode = nextNode;
    }

    /**
     * Get the var.
     * @return var.
     */
    public VariableNode getVar() {
        return var;
    }

    /**
     * Get start value.
     * @return start value.
     */
    public float getStart() {
        return start;
    }

    /**
     * Get stop value.
     * @return stop value.
     */
    public float getStop() {
        return stop;
    }

    /**
     * Get step value.
     * @return step value.
     */
    public float getStep() {
        return step;
    }

    /**
     * Get statement node.
     * @return statement node.
     */
    public StatementsNode getStatementsNode() {
        return statements;
    }

    /**
     * Get statement node's statement list.
     * @return statement node's statement list.
     */
    public ArrayList<StatementNode> getStatements(){
        return statements.getStatements();
    }

    /**
     * Get NEXT node.
     * @return NEXT node.
     */
    public NextNode getNextNode() {
        return nextNode;
    }

    /**
     * Get string representation of nodes.
     * @return string representation of nodes.
     */
    @Override
    public String toString() {
        return "For: { " +
                "var: \"" + var +
                "\", start: \"" + start +
                "\", stop: \"" + stop +
                "\", step: \"" + step +
                "\", statements: \"" + statements +
                "\", next: \"" + nextNode +
                "\" }";
    }
}
