/**
 * Abstract node representing a single statement.
 */
public abstract class StatementNode extends Node {

    private StatementNode nextStatement;

    /**
     * Setter for nextStatement.
     * @param node nextStatement.
     */
    public void setNextStatement(StatementNode node){
        this.nextStatement = node;
    }

    /**
     * Getter for nextStatement.
     * @return nextStatement.
     */
    public StatementNode getNextStatement(){
        return this.nextStatement;
    }
}
