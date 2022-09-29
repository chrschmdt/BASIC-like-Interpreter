import java.util.ArrayList;

/**
 * Parser that parses an ArrayList of Tokens.
 */
public class Parser {

    ArrayList<Token> tokens;

    /**
     * Constructor.
     * @param tokens list of tokens to parse.
     */
    public Parser(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * Helper fn that checks for specific token
     * and returns it if found.
     * @param type type of token to look for.
     * @return Token if matching, otherwise null.
     */
    private Token matchAndRemove(TokenType type){
        if (tokens.size() > 0 && tokens.get(0).getTokenType().equals(type))
            return tokens.remove(0);
        return null;
    }

    /**
     * Helper fn that puts a token back in the queue.
     * @param token to be put back.
     */
    private void pushFront(Token token){
        tokens.add(0, token);
    }

    /**
     * Fetches list of variables.
     * @return list of variables as node.
     * @throws Exception when comma is not followed.
     */
    private ReadNode readStatement() throws Exception{
        // check for token
        if (matchAndRemove(TokenType.READ) == null) return null;
        // make list to return
        ArrayList<VariableNode> nodes = new ArrayList<>();
        // get variable
        Token token = matchAndRemove(TokenType.IDENTIFIER);
        // if value is not null
        while (token != null){
            // add node and check for comma
            nodes.add(new VariableNode(token.getValue()));
            if (matchAndRemove(TokenType.COMMA) != null ) {
                // update node, if no variable exists then throw
                token = matchAndRemove(TokenType.IDENTIFIER);
                if (token == null) throw new Exception("Comma not followed by variable.");
            } else break;
        }
        // return nodes, or null if none exist
        if (nodes.size() > 0) return new ReadNode(nodes);
        else throw new Exception("READ statement empty.");
    }

    /**
     * Fetches comma delimited list of data.
     * @return list of data as nodes.
     * @throws Exception when comma is not followed.
     */
    private DataNode dataStatement() throws Exception{
        // check for token
        if (matchAndRemove(TokenType.DATA) == null) return null;
        // make list to return
        ArrayList<Node> nodes = new ArrayList<>();
        // get datum
        Token token = matchAndRemove(TokenType.STRING);
        if (token == null) token = matchAndRemove(TokenType.NUMBER);
        // if value is not null
        while (token != null){
            // determine type and add node
            if (token.getTokenType() == TokenType.STRING)
                nodes.add(new StringNode(token.getValue()));
            else {
                // parse number
                float numFloat = Float.parseFloat(token.getValue());
                // check if it is an int
                if (numFloat == Math.round(numFloat))
                    nodes.add(new IntegerNode(Math.round(numFloat)));
                else
                    nodes.add(new FloatNode(numFloat));
            }
            // check for comma
            if (matchAndRemove(TokenType.COMMA) != null ) {
                // update node, if no datum exists then throw
                token = matchAndRemove(TokenType.STRING);
                if (token == null) token = matchAndRemove(TokenType.NUMBER);
                if (token == null)
                    throw new Exception("Comma not followed by valid datum.");
            } else break;
        }
        // return nodes, or null if none exist
        if (nodes.size() > 0) return new DataNode(nodes);
        else throw new Exception("DATA statement empty.");
    }

    /**
     * Fetches comma delimited list of inputs.
     * @return list of inputs as nodes.
     * @throws Exception when comma is not followed.
     */
    private InputNode inputStatement() throws Exception{
        // check for token
        if (matchAndRemove(TokenType.INPUT) == null) return null;
        // make list to return
        ArrayList<Node> nodes = new ArrayList<>();
        // get input
        Token token = matchAndRemove(TokenType.STRING);
        if (token == null) token = matchAndRemove(TokenType.IDENTIFIER);
        // if value is not null
        while (token != null){
            // determine type and add node
            if (token.getTokenType() == TokenType.STRING)
                nodes.add(new StringNode(token.getValue()));
            else
                nodes.add(new VariableNode(token.getValue()));
            // check for comma
            if (matchAndRemove(TokenType.COMMA) != null ) {
                // update node, if no input exists then throw
                token = matchAndRemove(TokenType.IDENTIFIER);
                if (token == null)
                    throw new Exception("Comma not followed by valid input.");
            } else break;
        }
        // return nodes, or null if none exist
        if (nodes.size() > 0) return new InputNode(nodes);
        else throw new Exception("INPUT statement empty.");
    }

    /**
     * Fetches comma delimited list of expression for printing.
     * @return list of expressions as nodes.
     * @throws Exception when comma is not followed.
     */
    private ArrayList<Node> printList() throws Exception{
        // make list to return
        ArrayList<Node> nodes = new ArrayList<>();
        // get expression
        Node node = expression();
        // or, get string
        if (node == null) {
            Token token = matchAndRemove(TokenType.STRING);
            if (token != null) node = new StringNode(token.getValue());
        }
        // if value is not null
        while (node != null){
            // add node and check for comma
            nodes.add(node);
            if (matchAndRemove(TokenType.COMMA) != null ) {
                // update node, if no expression exists then throw
                node = expression();
                if (node == null) {
                    Token token = matchAndRemove(TokenType.STRING);
                    if (token != null) node = new StringNode(token.getValue());
                }
                if (node == null)
                    throw new Exception("Comma not followed by valid expression.");
            } else break;
        }
        // return nodes, or null if none exist
        return nodes.size() > 0 ? nodes : null;
    }

    /**
     * Checks for print statement.
     * @return respective node or null.
     * @throws Exception when comma syntax.
     */
    private PrintNode printStatement() throws Exception{
        // get print token or fail
        if (matchAndRemove(TokenType.PRINT) == null) return null;
        // get print list or fail
        ArrayList<Node> printList = printList();
        if (printList == null)
            throw new Exception("PRINT statement empty.");
        // return PrintNode of print list
        return new PrintNode(printList);
    }

    /**
     * Checks for assignment statement.
     * @return respective node or null.
     * @throws Exception when equals sign not followed by expression.
     */
    private AssignmentNode assignment() throws Exception{
        // match identifier for variable
        Token id = matchAndRemove(TokenType.IDENTIFIER);
        if (id != null) {
            // match equals sign
            Token eq = matchAndRemove(TokenType.EQUALS);
            if (eq != null) {
                // match expression
                Node exp = expression();
                if (exp != null)
                    return new AssignmentNode(new VariableNode(id.getValue()), exp);
                else throw new Exception("Expression not found in assignment.");
            }
        }
        // failed to parse assignment node
        return null;
    }

    /**
     * Checks for gosub statement.
     * @return node for statement or null.
     * @throws Exception when GOSUB not appropriately followed.
     */
    private GoSubNode goSubStatement() throws Exception{
        // get gosub
        if (matchAndRemove(TokenType.GOSUB) == null) return null;
        // get identifier
        Token id = matchAndRemove(TokenType.IDENTIFIER);
        if (id == null) throw new Exception("GOSUB not followed by identifier.");
        // make node
        return new GoSubNode(id.getValue());
    }

    /**
     * Check for return statement.
     * @return node for statement or null.
     */
    private ReturnNode returnStatement(){
        Token ret = matchAndRemove(TokenType.RETURN);
        return ret == null ? null : new ReturnNode();
    }

    /**
     * Helper fn that gets appropriate next statement.
     * @param var variable of loop to be closed.
     * @return node of next statement.
     * @throws Exception when invalid syntax.
     */
    private NextNode getNext(String var) throws Exception{
        // get next token
        Token next = matchAndRemove(TokenType.NEXT);
        if (next != null){
            // get variable
            Token id = matchAndRemove(TokenType.IDENTIFIER);
            if (id != null){
                // check if variable is correct
                if (id.getValue().equals(var))
                    return new NextNode(new VariableNode(var));
                else {
                    pushFront(id);
                    pushFront(next);
                    return null;
                }
            }
            else throw new Exception("Expected variable after NEXT statement.");
        }
        return null;
    }

    /**
     * Checks for for loop statement.
     * @return for loop node.
     * @throws Exception when invalid syntax.
     */
    private ForNode forLoopStatement() throws Exception{
        // get for token
        if (matchAndRemove(TokenType.FOR) == null) return null;
        // get variable name
        Token var = matchAndRemove(TokenType.IDENTIFIER);
        if (var == null)
            throw new Exception("FOR loop not given variable.");
        // get equals sign
        if (matchAndRemove(TokenType.EQUALS) == null)
            throw new Exception("FOR variable not followed by equals.");
        // get start value
        Token startToken = matchAndRemove(TokenType.NUMBER);
        float start;
        if (startToken == null)
            throw new Exception("FOR loop not given start value.");
        try {
            start = Float.parseFloat(startToken.getValue());
        } catch (Exception e){
            throw new Exception("FOR loop start value must be a number.");
        }
        // get "to"
        if (matchAndRemove(TokenType.TO) == null)
            throw new Exception("FOR loop init has invalid syntax.");
        // get stop value
        Token stopToken = matchAndRemove(TokenType.NUMBER);
        float stop;
        if (stopToken == null)
            throw new Exception("FOR loop not given stop value.");
        try {
            stop = Float.parseFloat(stopToken.getValue());
        } catch (Exception e){
            throw new Exception("FOR loop stop value must be a number.");
        }
        // get step
        float step = 1;
        if (matchAndRemove(TokenType.STEP) != null) {
            // attempt to parse step value from number
            try {
                Token stepNumber = matchAndRemove(TokenType.NUMBER);
                if (stepNumber != null) step = Float.parseFloat(stepNumber.getValue());
                else throw new Exception("FOR loop step value must be a number.");
            } catch (Exception e) {
                throw new Exception("FOR loop step value must be a number.");
            }
        }
        // get statements
        StatementsNode statements = statements();
        NextNode nextNode = getNext(var.getValue());
        if (nextNode == null)
            throw new Exception("Expected NEXT after FOR.");
        // return for node
        return new ForNode(
                nextNode.getVar(),
                start,
                stop,
                step,
                statements,
                nextNode
        );
    }

    /**
     * Checks for if statement;
     * @return node for statement or null.
     * @throws Exception when invalid syntax.
     */
    private IfNode ifStatement() throws Exception{
        // match if call
        Token ifToken = matchAndRemove(TokenType.IF);
        if (ifToken == null) return null;
        // get boolean expression
        BooleanOperationNode booleanOp = booleanExpression();
        // match then call
        Token thenToken = matchAndRemove(TokenType.THEN);
        if (thenToken == null) throw new Exception("Expected THEN after IF.");
        // get the label
        Token id = matchAndRemove(TokenType.IDENTIFIER);
        if (id == null) throw new Exception("Expected label after IF.");
        // make node
        return new IfNode(booleanOp, new VariableNode(id.getValue()));
    }

    /**
     * Checks for statements.
     * @return node of appropriate statement or null.
     * @throws Exception from calling fns.
     */
    private StatementNode statement() throws Exception{
        // fetch statement
        StatementNode statement = printStatement();
        if (statement == null) statement = assignment();
        if (statement == null) statement = readStatement();
        if (statement == null) statement = dataStatement();
        if (statement == null) statement = inputStatement();
        if (statement == null) statement = ifStatement();
        if (statement == null) statement = forLoopStatement();
        if (statement == null) statement = goSubStatement();
        if (statement == null) statement = returnStatement();
        // return statement
        return statement;
    }

    /**
     * Checks for statement until none remain.
     * @return node of statements.
     * @throws Exception from calling fns.
     */
    private StatementsNode statements() throws Exception{
        // make node to hold statements and declare first statement
        StatementsNode statementsNode = new StatementsNode();
        StatementNode node;
        // iterate until no statements remain
        while (true) {
            // get label
            Token label = matchAndRemove(TokenType.LABEL);
            // get node
            node = statement();
            // check if node is null and we're at end of line
            if (node == null && matchAndRemove(TokenType.EndOfLine) != null) continue;
            if (node == null) break;
            // check if label exists
            if (label == null) statementsNode.addNode(node);
            else statementsNode.addNode(new LabeledStatementNode(label.getValue(), node));
        }
        // return node of list
        return statementsNode;
    }

    /**
     * Helper fn that gets a boolean operator.
     * @return boolean operator.
     * @throws Exception when syntax invalid.
     */
    private BooleanOperationNode.Operator booleanOperator() throws Exception{
        // define acceptable types
        TokenType[] ops = {
                TokenType.EQUALS,
                TokenType.NOTEQUALS,
                TokenType.GT,
                TokenType.GTE,
                TokenType.LT,
                TokenType.LTE
        };
        // iterate thru types and try to match
        for (TokenType type : ops){
            Token token = matchAndRemove(type);
            if (token != null) return BooleanOperationNode.tokenOpMap.get(type);
        }
        // if no types match throw exception
        throw new Exception("Expected operator.");
    }

    /**
     * Helper fn that gets a boolean expression
     * @return boolean expression node.
     * @throws Exception when syntax invalid.
     */
    private BooleanOperationNode booleanExpression() throws Exception{
        // get right hand expression
        if (matchAndRemove(TokenType.LPAREN) == null)
            throw new Exception("Parentheses required for boolean expression.");
        Node left = expression();
        if (left == null) throw new Exception("Expected expression.");
        // get operator
        BooleanOperationNode.Operator op = booleanOperator();
        // get left hand expression
        Node right = expression();
        if (right == null) throw new Exception("Expected expression.");
        if (matchAndRemove(TokenType.RPAREN) == null)
            throw new Exception("Closing parentheses not found in boolean expression.");
        // return result
        return new BooleanOperationNode(left, right, op);
    }

    /**
     * Helper fn that looks for function invocation.
     * @return node for function.
     * @throws Exception on syntax error.
     */
    private FunctionNode functionInvocation() throws Exception{
        // check for fn token
        Token token = null;
        for (TokenType type : FunctionNode.fnTokens){
            token = matchAndRemove(type);
            if (token != null) break;
        }
        // if none found, return null
        if (token == null) return null;
        // get rparen
        if (matchAndRemove(TokenType.LPAREN) == null)
            throw new Exception("Expected paren after function invocation.");
        // make param list
        ArrayList<Node> params = new ArrayList<>();
        Node param = string();
        if (param == null) param = expression();
        // loop to get params
        while (param != null){
            params.add(param);
            Token comma = matchAndRemove(TokenType.COMMA);
            // check for comma
            if (comma != null){
                param = string();
                if (param == null) param = expression();
                // check for new param
                if (param == null)
                    throw new Exception("Expected param after comma in fn invocation.");
            } else param = null;
        }
        // get lparen
        if (matchAndRemove(TokenType.RPAREN) == null)
            throw new Exception("Expected paren after function params.");
        // return node
        return new FunctionNode(params, token.getTokenType());
    }

    /**
     * Helper fn that parses a string.
     * @return string parsed or null.
     */
    private StringNode string() {
        Token str = matchAndRemove(TokenType.STRING);
        return str == null ? null : new StringNode(str.getValue());
    }

    /**
     * Helper fn that parses an expression.
     * @return Node of expression.
     * @throws Exception when there is a syntax error.
     */
    private Node expression() throws Exception{
        // check for term
        Node term = term();
        // if term is found
        if (term != null) {
            // loop to look for more terms
            while (true){
                // get operator
                Token op = matchAndRemove(TokenType.ADD);
                if (op == null) op = matchAndRemove(TokenType.SUBTRACT);
                // if no operator
                if (op == null) {
                    // check for potential negative number as minus operation
                    Token potentialMinus = matchAndRemove(TokenType.NUMBER);
                    if (potentialMinus != null){
                        // found attached minus
                        if (((Character) potentialMinus.getValue().charAt(0)).equals('-')){
                            op = new Token(TokenType.SUBTRACT);
                            pushFront(new Token(TokenType.NUMBER, potentialMinus.getValue().substring(1)));
                        }
                        // did not find
                        else pushFront(potentialMinus);
                    }
                    else return term;
                }
                // if there is one, get the following term
                if (op != null) {
                    Node term2 = term();
                    if (term2 != null)
                        term = new MathOpNode(MathOpNode.tokenOpMap.get(op.getTokenType()), term, term2);
                    // if no following term, there is an error
                    else throw new Exception("Expected valid term after operation.");
                }
            }
        }
        else return functionInvocation();
    }

    /**
     * Helper fn that parses a term.
     * @return Node of term.
     * @throws Exception when there is a syntax error.
     */
    private Node term() throws Exception{
        // check for factor
        Node factor = factor();
        // if factor is found
        if (factor != null) {
            // loop to look for more factors
            while (true){
                // get operator
                Token op = matchAndRemove(TokenType.MULTIPLY);
                if (op == null) op = matchAndRemove(TokenType.DIVIDE);
                // if no operator, return what we have
                if (op == null) return factor;
                    // if there is one, get the following term
                else {
                    Node factor2 = factor();
                    if (factor2 != null)
                        factor = new MathOpNode(MathOpNode.tokenOpMap.get(op.getTokenType()), factor, factor2);
                        // if no following factor, there is an error
                    else throw new Exception("Expected valid factor after operation.");
                }
            }
        }
        else return null;
    }

    /**
     * Helper fn that parses a factor.
     * @return Node of factor.
     * @throws Exception when there is a syntax error.
     */
    private Node factor() throws Exception{
        // try to match number
        Token token = matchAndRemove(TokenType.NUMBER);
        // if we did not match a number check for expression or identifier
        if (token == null){
            // try to match identifier
            token = matchAndRemove(TokenType.IDENTIFIER);
            // if we did not match an identifier check for expression
            if (token == null){
                // check for lparen
                Token lparen = matchAndRemove(TokenType.LPAREN);
                // if we found one
                if (lparen != null) {
                    Node expression = expression();
                    Token rparen = matchAndRemove(TokenType.RPAREN);
                    if (rparen != null) return expression;
                    else throw new Exception("No closing parenthesis to match open one.");
                }
                // otherwise we found no factor
                return null;
            } else return new VariableNode(token.getValue());
        }
        // we found a number, parse it
        float numFloat = Float.parseFloat(token.getValue());
        // check if it is an int
        if (numFloat == Math.round(numFloat)) return new
                IntegerNode(Math.round(numFloat));
        return new FloatNode(numFloat);
    }

    /**
     * Method called to parse the given list of tokens.
     * @return Highest level node of expression.
     * @throws Exception when there is a syntax error.
     */
    public StatementsNode parse() throws Exception{
        return statements();
    }
}
