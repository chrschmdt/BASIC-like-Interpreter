import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Lexer class with function for lexing strings to tokens.
 */
public class Lexer {

    // "state machine" member vars
    StringBuilder numberState = new StringBuilder();
    StringBuilder stringState = new StringBuilder();
    StringBuilder wordState = new StringBuilder();
    ArrayList<Token> tokens = new ArrayList<>();
    Boolean takingString = false;

    // static member vars
    static final Pattern number = Pattern.compile("[0-9]");
    static final Pattern letter = Pattern.compile("[a-zA-Z]");
    static HashMap<String, TokenType> keywords = new HashMap<>();
    static {
        // data io
        keywords.put("PRINT", TokenType.PRINT);
        keywords.put("INPUT", TokenType.INPUT);
        keywords.put("READ", TokenType.READ);
        keywords.put("DATA", TokenType.DATA);
        // control
        keywords.put("GOSUB", TokenType.GOSUB);
        keywords.put("RETURN", TokenType.RETURN);
        // loops
        keywords.put("FOR", TokenType.FOR);
        keywords.put("NEXT", TokenType.NEXT);
        keywords.put("STEP", TokenType.STEP);
        keywords.put("TO", TokenType.TO);
        // conditionals
        keywords.put("IF", TokenType.IF);
        keywords.put("THEN", TokenType.THEN);
        // functions
        keywords.put("RANDOM", TokenType.FN_RANDOM);
        keywords.put("LEFT$", TokenType.FN_LEFT);
        keywords.put("RIGHT$", TokenType.FN_RIGHT);
        keywords.put("MID$", TokenType.FN_MID);
        keywords.put("NUM$", TokenType.FN_NUM);
        keywords.put("VAL", TokenType.FN_VAL);
        keywords.put("VAL%", TokenType.FN_VAL_F);
    }

    /**
     * Helper function that tokenizes number state value and clears it.
     */
    private void pushNumber(){
        if (numberState.length() > 0) {
            tokens.add(new Token(TokenType.NUMBER, numberState.toString()));
            numberState.setLength(0);
        }
    }

    /**
     * Helper function that tokenizes current word and clears it.
     */
    private void pushWord(){
        if (wordState.length() > 0) {
            final String word = wordState.toString();
            if (keywords.containsKey(word))
                tokens.add(new Token(keywords.get(word)));
            else if (word.contains(":"))
                tokens.add(new Token(TokenType.LABEL, word.substring(0, word.length()-1)));
            else
                tokens.add(new Token(TokenType.IDENTIFIER, word));
            wordState.setLength(0);
        }
    }

    /**
     * Helper function that pushes both word and number.
     */
    private void pushBoth(){
        pushWord();
        pushNumber();
    }

    /**
     * Helper function that tokenizes current string and clears it.
     */
    private void pushString(){
        final String string = stringState.toString();
        tokens.add(new Token(TokenType.STRING, string));
        stringState.setLength(0);
    }

    /**
     * Takes a string and lexes to a list of tokens defined in TokenType enum.
     * @param string string to be lexed.
     * @return list of tokens from string.
     * @throws Exception when lexing error.
     */
    public ArrayList<Token> lex(String string) throws Exception{

        // iterate through characters
        for (int i = 0; i < string.length(); i++){
            char c = string.charAt(i);
            // check if we're dealing with a string
            if (c == '"') {
                if (takingString) pushString();
                takingString = !takingString;
            }
            else if (takingString) stringState.append(c);
            // if we are not dealing with a string
            else switch (c){
                case ' ':
                    pushBoth();
                    break;
                case ',':
                    pushBoth();
                    tokens.add(new Token(TokenType.COMMA));
                    break;
                case '+':
                    pushBoth();
                    tokens.add(new Token(TokenType.ADD));
                    break;
                case '*':
                    pushBoth();
                    tokens.add(new Token(TokenType.MULTIPLY));
                    break;
                case '/':
                    pushBoth();
                    tokens.add(new Token(TokenType.DIVIDE));
                    break;
                case '=':
                    pushBoth();
                    tokens.add(new Token(TokenType.EQUALS));
                    break;
                case '(':
                    pushBoth();
                    tokens.add(new Token(TokenType.LPAREN));
                    break;
                case ')':
                    pushBoth();
                    tokens.add(new Token(TokenType.RPAREN));
                    break;
                case '$':
                case '%':
                case ':':
                    if (wordState.length() > 0){
                        wordState.append(c);
                        pushWord();
                    }
                    else throw new Exception("Invalid input at \"" + c +"\"");
                    break;
                case '-':
                    // are we taking a word
                    if (wordState.length() > 0) pushWord();
                    // is next char is number (this is negative sign)
                    if (numberState.length() == 0
                            && i < string.length() -1
                            && number.matcher(Character.toString(string.charAt(i+1))).find())
                        numberState.append("-");
                    // otherwise this is a minus sign
                    else {
                        pushNumber();
                        tokens.add(new Token(TokenType.SUBTRACT));
                    }
                    break;
                case '.':
                    // if we have number and last char of it isn't a dot
                    if ((numberState.length() > 0)
                            && (number.matcher(numberState.substring(numberState.length()-1)).find()))
                        numberState.append(c);
                    // otherwise input must be invalid
                    else throw new Exception("Invalid input at \"" + c +"\"");
                    break;
                case '>':
                    pushBoth();
                    // is next char '='
                    if (i < string.length() -1 && string.charAt(i+1) == '='){
                        tokens.add(new Token(TokenType.GTE));
                        i++;
                    }
                    else
                        tokens.add(new Token(TokenType.GT));
                    break;
                case '<':
                    pushBoth();
                    // is next char '='
                    if (i < string.length() -1 && string.charAt(i+1) == '='){
                        tokens.add(new Token(TokenType.LTE));
                        i++;
                    }
                    // is next char '>'
                    else if (i < string.length() -1 && string.charAt(i+1) == '>'){
                        tokens.add(new Token(TokenType.NOTEQUALS));
                        i++;
                    }
                    else
                        tokens.add(new Token(TokenType.LT));
                    break;
                default:
                    // if char is a number, add it to number state
                    if (number.matcher(Character.toString(c)).find()) {
                        pushWord();
                        numberState.append(c);
                    }
                    // if char is a letter, add it to word state
                    else if (letter.matcher(Character.toString(c)).find()) {
                        pushNumber();
                        wordState.append(c);
                    }
                    // otherwise input is invalid
                    else throw new Exception("Invalid input at \"" + c + "\"");
            }
        }

        // throw error if string is open
        if (takingString) throw new Exception("Invalid input: string is unclosed");

        // send last tokens, add EndOfLine and return list
        pushBoth();
        tokens.add(new Token(TokenType.EndOfLine));
        return tokens;
    }

}
