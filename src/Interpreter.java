import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class is the interpreter for the AST.
 */
public class Interpreter {

    // top node of AST
    private final StatementsNode top;

    // variable and label maps
    private final HashMap<String, Integer> intMap = new HashMap<>();
    private final HashMap<String, Float> floatMap = new HashMap<>();
    private final HashMap<String, String> stringMap = new HashMap<>();
    private final HashMap<String, StatementNode> labelMap = new HashMap<>();

    // list for data nodes
    private final ArrayList<Node> dataNodes = new ArrayList<>();

    /**
     * Constructor.
     * @param top AST node at top of tree.
     */
    public Interpreter(StatementsNode top){
        this.top = top;
    }

    /**
     * Helper fn to get node from front of queue.
     * @return node from data nodes list.
     */
    private Node getData(){
        if (dataNodes.isEmpty()) return null;
        return this.dataNodes.remove(0);
    }

    /**
     * Function that walks tree and handles labels.
     * @param nodeWS top node of tree to walk.
     */
    private void walkLabels(NodeWithStatements nodeWS){
        // iterate over nodes
        for (int i = 0; i < nodeWS.getStatements().size(); i++){
            // get current node
            StatementNode node = nodeWS.getStatements().get(i);
            // if node is labeled statement
            if (node instanceof LabeledStatementNode){
                // add to labelMap
                labelMap.put(
                        ((LabeledStatementNode) node).getLabel(),
                        ((LabeledStatementNode) node).getNode()
                );
                // replace node with child
                nodeWS.getStatements().set(i, ((LabeledStatementNode) node).getNode());
            }
            // if node itself has statements, recur
            if (node instanceof NodeWithStatements)
                walkLabels((NodeWithStatements) node);
        }
    }

    /**
     * Function that walks tree and handles for statements.
     * @param nodeWS top node of tree to walk.
     */
    private void walkFor(NodeWithStatements nodeWS){
        // iterate over nodes
        int nodeCount = nodeWS.getStatements().size();
        for (int i = 0; i < nodeCount; i++){
            // get current node
            StatementNode node = nodeWS.getStatements().get(i);
            // if node is FOR node
            if (node instanceof ForNode && i < nodeCount-1){
                // set next statement for this node
                node.setNextStatement(nodeWS.getStatements().get(i+1));
                // set next statement for this node's NEXT node
                ((ForNode) node).getNextNode().setNextStatement(nodeWS.getStatements().get(i+1));
            }
            // if node itself has statements, recur
            if (node instanceof NodeWithStatements)
                walkFor((NodeWithStatements) node);
        }
    }

    /**
     * Function that walks tree and handles data.
     * @param nodeWS top node of tree to walk.
     */
    private void walkData(NodeWithStatements nodeWS){
        // iterate over nodes
        for (int i = 0; i < nodeWS.getStatements().size(); i++){
            // get current node
            StatementNode node = nodeWS.getStatements().get(i);
            // if node is DATA node
            if (node instanceof DataNode) {
                dataNodes.addAll(((DataNode) node).getNodes());
                nodeWS.getStatements().remove(i--);
            }
            // if node itself has statements, recur
            if (node instanceof NodeWithStatements)
                walkData((NodeWithStatements) node);
        }
    }

    /**
     * Function that walks tree and sets next node for each node.
     * @param nodeWS top node of tree to walk.
     */
    private void walkSetNext(NodeWithStatements nodeWS){
        // iterate over nodes
        int nodeCount = nodeWS.getStatements().size();
        for (int i = 0; i < nodeCount; i++){
            // get current node
            StatementNode node = nodeWS.getStatements().get(i);
            // set nextStatement for node
            if (i < nodeCount - 1)
                node.setNextStatement(nodeWS.getStatements().get(i+1));
            else
                node.setNextStatement(null);
            // if node itself has statements, recur
            if (node instanceof NodeWithStatements)
                walkSetNext((NodeWithStatements) node);
        }
    }

    /**
     * Helper fn that takes function node and returns its result
     * @param node function node.
     * @return result of function as a node.
     * @throws Exception when invalid syntax.
     */
    private Node function(FunctionNode node) throws Exception{
        switch (node.getFunction()){
            case FN_RANDOM:
                return new IntegerNode((int)Math.floor(Math.random()*1000)+1);
            case FN_LEFT:
                // test params
                if (node.getParams().size() != 2 ||
                        !(node.getParams().get(0) instanceof StringNode &&
                                node.getParams().get(1) instanceof IntegerNode))
                    throw new Exception("Unexpected number of arguments to function.");
                // get params and return substring
                String fnLeftString = ((StringNode) node.getParams().get(0)).getString();
                Integer fnLeftInteger = ((IntegerNode) node.getParams().get(1)).getInteger();
                return new StringNode(fnLeftString.substring(0, fnLeftInteger));
            case FN_RIGHT:
                // test params
                if (node.getParams().size() != 2 ||
                        !(node.getParams().get(0) instanceof StringNode &&
                                node.getParams().get(1) instanceof IntegerNode))
                    throw new Exception("Unexpected number of arguments to function.");
                // get params and return substring
                String fnRightString = ((StringNode) node.getParams().get(0)).getString();
                Integer fnRightInteger = ((IntegerNode) node.getParams().get(1)).getInteger();
                return new StringNode(fnRightString.substring(fnRightInteger));
            case FN_MID:
                // test params
                if (node.getParams().size() != 3 ||
                        !(node.getParams().get(0) instanceof StringNode &&
                                node.getParams().get(1) instanceof IntegerNode &&
                                node.getParams().get(2) instanceof IntegerNode))
                    throw new Exception("Unexpected number of arguments to function.");
                // get params and return substring
                String fnMidString = ((StringNode) node.getParams().get(0)).getString();
                Integer fnMidInteger1 = ((IntegerNode) node.getParams().get(1)).getInteger();
                Integer fnMidInteger2 = ((IntegerNode) node.getParams().get(2)).getInteger();
                return new StringNode(fnMidString.substring(fnMidInteger1, fnMidInteger2));
            case FN_NUM:
                // test params
                if (node.getParams().size() != 1 ||
                        !(node.getParams().get(0) instanceof IntegerNode ||
                                node.getParams().get(0) instanceof FloatNode))
                    throw new Exception("Unexpected number of arguments to function.");
                // get param and return string node
                Node fnNumNode = node.getParams().get(0);
                return new StringNode(
                        fnNumNode instanceof IntegerNode ?
                                ((IntegerNode) fnNumNode).getInteger().toString() :
                                ((FloatNode) fnNumNode).getFloat().toString());
            case FN_VAL:
                // test params
                if (node.getParams().size() != 1 ||
                        !(node.getParams().get(0) instanceof StringNode))
                    throw new Exception("Unexpected number of arguments to function.");
                return new IntegerNode(Integer.parseInt(((StringNode) node.getParams().get(0)).getString()));
            case FN_VAL_F:
                // test params
                if (node.getParams().size() != 1 ||
                        !(node.getParams().get(0) instanceof StringNode))
                    throw new Exception("Unexpected number of arguments to function.");
                return new FloatNode(Float.parseFloat(((StringNode) node.getParams().get(0)).getString()));
        }
        return null;
    }

    /**
     * Evaluates an int equivalent.
     * @param node node to evaluate.
     * @return int equivalent.
     * @throws Exception on invalid syntax.
     */
    private IntegerNode evaluateIntMathOp(Node node) throws Exception{
        // is this an integer
        if (node instanceof IntegerNode) return (IntegerNode) node;
        // is this a variable for an integer
        else if (node instanceof VariableNode) {
            String name = ((VariableNode) node).getName();
            if (intMap.containsKey(name)) return new IntegerNode(intMap.get(name));
            else throw new Exception("Variable '" + name + "' not found.");
        }
        // is this a math op that results in integer
        else if (node instanceof MathOpNode){
            Node mathOpResult = mathOp((MathOpNode) node);
            if (mathOpResult instanceof IntegerNode)
                return (IntegerNode) mathOpResult;
        }
        return null;
    }

    /**
     * Evaluates a float equivalent.
     * @param node node to evaluate.
     * @return float equivalent.
     * @throws Exception on invalid syntax.
     */
    private FloatNode evaluateFloatMathOp(Node node) throws Exception{
        // is this a float
        if (node instanceof FloatNode) return (FloatNode) node;
        // is this a variable for a float
        else if (node instanceof VariableNode) {
            String name = ((VariableNode) node).getName();
            if (floatMap.containsKey(name)) return new FloatNode(floatMap.get(name));
            else throw new Exception("Variable '" + name + "' not found.");
        }
        // is this a math op that results in float
        else if (node instanceof MathOpNode){
            Node mathOpResult = mathOp((MathOpNode) node);
            if (mathOpResult instanceof FloatNode)
                return (FloatNode) mathOpResult;
        }
        return null;
    }

    /**
     * Executes a math operation recursively.
     * @param node math node to execute.
     * @return numeric type node.
     * @throws Exception on invalid syntax.
     */
    private Node mathOp(MathOpNode node) throws Exception{

        // define sides
        float left;
        float right;
        float result;

        // check if we're staying as int
        boolean keepInt = (node.getLeft() instanceof IntegerNode &&
                node.getRight() instanceof IntegerNode && (
                        node.getOperation().equals(MathOpNode.Operation.ADD) ||
                        node.getOperation().equals(MathOpNode.Operation.SUBTRACT)));

        // check if left is number
        if (node.getLeft() instanceof IntegerNode) left = (float) ((IntegerNode) node.getLeft()).getInteger();
        else if (node.getLeft() instanceof FloatNode) left = ((FloatNode) node.getLeft()).getFloat();
        // check if left is var
        else if (node.getLeft() instanceof VariableNode) {
            String name = ((VariableNode) node.getLeft()).getName();
            if (intMap.containsKey(name)) left = intMap.get(name);
            else if (floatMap.containsKey(name)) left = floatMap.get(name);
            else throw new Exception("Variable '" + name + "' not found.");
        }
        // check if left is fn
        else if (node.getLeft() instanceof FunctionNode){
            Node fnResult = function((FunctionNode) node.getLeft());
            if (fnResult instanceof IntegerNode) left = ((IntegerNode) fnResult).getInteger();
            else if (fnResult instanceof FloatNode) left = ((FloatNode) fnResult).getFloat();
            else throw new Exception("Function does not return numeric type.");
        }
        else {
            Node opResult = mathOp((MathOpNode) node.getLeft());
            if (opResult instanceof IntegerNode) left = (float) ((IntegerNode) opResult).getInteger();
            else if (opResult instanceof FloatNode) left = ((FloatNode) opResult).getFloat();
            else throw new Exception("Math operation has invalid syntax.");
        }

        // check if right is number
        if (node.getRight() instanceof IntegerNode) right = (float) ((IntegerNode) node.getRight()).getInteger();
        else if (node.getRight() instanceof FloatNode) right = ((FloatNode) node.getRight()).getFloat();
        // check if right is var
        else if (node.getRight() instanceof VariableNode) {
            String name = ((VariableNode) node.getRight()).getName();
            if (intMap.containsKey(name)) right = intMap.get(name);
            else if (floatMap.containsKey(name)) right = floatMap.get(name);
            else throw new Exception("Variable '" + name + "' not found.");
        }
        // check if right is fn
        else if (node.getRight() instanceof FunctionNode){
            Node fnResult = function((FunctionNode) node.getRight());
            if (fnResult instanceof IntegerNode) right = ((IntegerNode) fnResult).getInteger();
            else if (fnResult instanceof FloatNode) right = ((FloatNode) fnResult).getFloat();
            else throw new Exception("Function does not return numeric type.");
        }
        else {
            Node opResult = mathOp((MathOpNode) node.getRight());
            if (opResult instanceof IntegerNode) right = (float) ((IntegerNode) opResult).getInteger();
            else if (opResult instanceof FloatNode) right = ((FloatNode) opResult).getFloat();
            else throw new Exception("Math operation has invalid syntax.");
        }

        // do operation
        switch (node.getOperation()){
            case ADD:
                result = left + right;
                break;
            case SUBTRACT:
                result = left - right;
                break;
            case MULTIPLY:
                result = left * right;
                break;
            case DIVIDE:
                result = left / right;
                break;
            default:
                throw new Exception("Invalid operation.");
        }

        // return result
        return keepInt ? new IntegerNode((int) result) : new FloatNode(result);
    }

    /**
     * Evaluates a boolean node.
     * @param node boolean node.
     * @return Boolean evaluated.
     * @throws Exception on invalid syntax.
     */
    private boolean evaluateBoolean(BooleanOperationNode node) throws Exception {

        // handle if sides are functions
        if (node.getLeft() instanceof FunctionNode)
            node.setLeft(function((FunctionNode) node.getLeft()));
        if (node.getRight() instanceof FunctionNode)
            node.setRight(function((FunctionNode) node.getRight()));

        // handle if sides are math op
        if (node.getLeft() instanceof MathOpNode)
            node.setLeft(mathOp((MathOpNode) node.getLeft()));
        if (node.getRight() instanceof MathOpNode)
            node.setRight(mathOp((MathOpNode) node.getRight()));

        // handle if sides are variable
        if (node.getLeft() instanceof VariableNode){
            String name = ((VariableNode) node.getLeft()).getName();
            if (intMap.containsKey(name)) node.setLeft(new IntegerNode(intMap.get(name)));
            else if (floatMap.containsKey(name)) node.setLeft(new FloatNode(floatMap.get(name)));
            else throw new Exception("Variable '" + name + "' not found.");
        }
        if (node.getRight() instanceof VariableNode){
            String name = ((VariableNode) node.getRight()).getName();
            if (intMap.containsKey(name)) node.setRight(new IntegerNode(intMap.get(name)));
            else if (floatMap.containsKey(name)) node.setRight(new FloatNode(floatMap.get(name)));
            else throw new Exception("Variable '" + name + "' not found.");
        }

        // handle sides of type number
        float left, right;
        // start with left
        if (node.getLeft() instanceof IntegerNode)
            left = (float) ((IntegerNode) node.getLeft()).getInteger();
        else if (node.getLeft() instanceof FloatNode)
            left = ((FloatNode) node.getLeft()).getFloat();
        else throw new Exception("Invalid boolean expression on left side.");
        // then right
        if (node.getRight() instanceof IntegerNode)
            right = (float) ((IntegerNode) node.getRight()).getInteger();
        else if (node.getRight() instanceof FloatNode)
            right = ((FloatNode) node.getRight()).getFloat();
        else throw new Exception("Invalid boolean expression on right side.");

        // handle operator and return result
        switch (node.getOperator()){
            case GT:
                return left > right;
            case GTE:
                return left >= right;
            case LT:
                return left < right;
            case LTE:
                return left <= right;
            case EQUALS:
                return left == right;
            case NOTEQUALS:
                return left != right;
            default:
                throw new Exception("Boolean operation invalid.");
        }
    }

    /**
     * Helper fn that prints a node.
     * @param node to print.
     * @throws Exception when invalid syntax.
     */
    private void printNode(Node node, String prepend) throws Exception {
        // print thing to prepend
        if (prepend != null)
            System.out.print(prepend);
        // print according to node type
        if (node instanceof StringNode)
            System.out.println(((StringNode) node).getString());
        else if (node instanceof IntegerNode)
            System.out.println(((IntegerNode) node).getInteger());
        else if (node instanceof FloatNode)
            System.out.println(((FloatNode) node).getFloat());
        else if (node instanceof FunctionNode)
            printNode(function((FunctionNode) node), null);
        else if (node instanceof MathOpNode)
            printNode(mathOp((MathOpNode) node), null);
        else if (node instanceof VariableNode){
            String key = ((VariableNode) node).getName();
            if (intMap.containsKey(key))
                System.out.println(intMap.get(key));
            else if (floatMap.containsKey(key))
                System.out.println(floatMap.get(key));
            else if (stringMap.containsKey(key))
                System.out.println(stringMap.get(key));
            else
                System.err.println("Variable '" + key + "' does not exist.");
        }
        else System.out.print(node.toString());
    }

    /**
     * Determines type of statement and executes it.
     * @param node statement to be executed.
     * @throws Exception on invalid syntax.
     */
    private void interpret(StatementNode node) throws Exception {

        // check if node is read node
        if (node instanceof ReadNode){
            for (VariableNode readable : ((ReadNode) node).getNodes()){
                // define name and data
                Node data = getData();
                String name = readable.getName();
                // check for integer
                if (data instanceof IntegerNode){
                    if (!intMap.containsKey(name))
                        intMap.put(name, ((IntegerNode) data).getInteger());
                    intMap.replace(name, ((IntegerNode) data).getInteger());
                }
                // check for float
                else if (data instanceof FloatNode){
                    if (!floatMap.containsKey(name))
                        floatMap.put(name, ((FloatNode) data).getFloat());
                    floatMap.replace(name, ((FloatNode) data).getFloat());
                }
                // check for string
                else if (data instanceof StringNode){
                    if (!stringMap.containsKey(name))
                        stringMap.put(name, ((StringNode) data).getString());
                    stringMap.replace(name, ((StringNode) data).getString());
                }
                else throw new Exception("Node '" + name + "' is of unsupported type.");
            }
        }

        // check if node is assignment node
        else if (node instanceof AssignmentNode){
            // get assignment values
            Node value = ((AssignmentNode) node).getValue();
            String name = ((AssignmentNode) node).getVariable().getName();
            // check type and add to appropriate map
            if (value instanceof FloatNode){
                if (floatMap.containsKey(name)) floatMap.replace(name, ((FloatNode) value).getFloat());
                else floatMap.put(name, ((FloatNode) value).getFloat());
            }
            else if (value instanceof IntegerNode){
                if (intMap.containsKey(name)) intMap.replace(name, ((IntegerNode) value).getInteger());
                else intMap.put(name, ((IntegerNode) value).getInteger());
            }
            else if (value instanceof StringNode){
                if (stringMap.containsKey(name)) stringMap.replace(name, ((StringNode) value).getString());
                else stringMap.put(name, ((StringNode) value).getString());
            }
            else if (value instanceof MathOpNode){
                Node eval = mathOp((MathOpNode) value);
                if (eval instanceof IntegerNode){
                    if (intMap.containsKey(name)) intMap.replace(name, ((IntegerNode) eval).getInteger());
                    else intMap.put(name, ((IntegerNode) eval).getInteger());
                }
                else if (eval instanceof FloatNode){
                    if (floatMap.containsKey(name)) floatMap.replace(name, ((FloatNode) eval).getFloat());
                    else floatMap.put(name, ((FloatNode) eval).getFloat());
                }
            }
        }

        // check if node is input node
        else if (node instanceof InputNode){
            // if variables are preceded by string, print it
            if (!((InputNode) node).getNodes().isEmpty() && ((InputNode) node).getNodes().get(0) instanceof StringNode)
                System.out.println(((StringNode) ((InputNode) node).getNodes().remove(0)).getString());
            // if node list is still not empty
            if (!((InputNode) node).getNodes().isEmpty()) {
                // instantiate scanner
                Scanner in = new Scanner(System.in);
                // iterate thru var nodes and get string for them
                for (Node var : ((InputNode) node).getNodes()) {
                    String varName = ((VariableNode) var).getName();
                    System.out.print(varName + ": ");
                    stringMap.put(varName, in.nextLine());
                    System.out.print("\n");
                }
            }
        }

        // check if node is print node
        else if (node instanceof PrintNode){
            // state that we're printing the node and iterate thru items
            System.out.println("Printing:");
            for (Node printable : ((PrintNode) node).getNodes())
                printNode(printable, "\t");
        }

    }

    /**
     * Execute statements ina statements node.
     * @param statementsNode to be executed.
     * @throws Exception on invalid syntax.
     */
    private void executeStatements(StatementsNode statementsNode) throws Exception{
        // make stack and pointer
        Stack<StatementNode> stack = new Stack<>();
        StatementNode current = statementsNode.getStatements().get(0);
        // execute current node
        while (current != null){
            if (current instanceof IfNode){
                // check truth value
                boolean truthVal = evaluateBoolean(((IfNode) current).getCondition());
                if (truthVal) {
                    // get node to go to and set as current
                    String label = ((IfNode) current).getLabel().getName();
                    if (!labelMap.containsKey(label))
                        throw new Exception("Label '" + label + "' not found.");
                    // go to labelled node
                    current = labelMap.get(label);
                } else current = current.getNextStatement();
            }
            else if (current instanceof ForNode){
                // get for loop params
                float start = ((ForNode) current).getStart();
                float stop = ((ForNode) current).getStop();
                float step = ((ForNode) current).getStep();
                boolean isIncreasing = stop >= start;
                String varName = ((ForNode) current).getVar().getName();
                // make var in map for internal access
                floatMap.put(varName, 0f);
                // make for loop
                for (float f = start; isIncreasing ? (f < stop) : (f > stop); f += step){
                    // update internal var
                    floatMap.replace(varName, f);
                    executeStatements(((ForNode) current).getStatementsNode());
                }
                // clean up and continue
                floatMap.remove(varName);
                current = current.getNextStatement();
            }
            else if (current instanceof GoSubNode){
                // set node to return to
                stack.push(current.getNextStatement());
                // get node to go to and set as current
                String label = ((GoSubNode) current).getLabel();
                if (!labelMap.containsKey(label))
                    throw new Exception("Label '" + label + "' not found.");
                current = labelMap.get(label);
            }
            else if (current instanceof ReturnNode){
                // ensure there is a node to goto
                if (stack.size() == 0)
                    throw new Exception("Invalid RETURN: No node exists to return to.");
                // goto that node
                current = stack.pop();
            }
            else {
                interpret(current);
                current = current.getNextStatement();
            }
        }
    }

    /**
     * Starting point for interpreter to walk.
     */
    public void initialize() throws Exception{

        // validate size
        if (top.getStatements().size() == 0) return;

        // do edits to tree
        walkLabels(top);
        walkFor(top);
        walkData(top);
        walkSetNext(top);

        // execute from top
        executeStatements(top);
    }
}
