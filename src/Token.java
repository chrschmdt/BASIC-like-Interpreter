/**
 * This class represents a token with a type and
 * a potential payload.
 */
public class Token {

    private final TokenType tokenType;
    private final String value;

    public Token(TokenType tokenType, String string){
        this.tokenType = tokenType;
        this.value = string;
    }

    public Token(TokenType tokenType){
        this(tokenType, null);
    }

    /**
     * Getter for token type.
     * @return type of token from enum.
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Getter for string value of token.
     * @return value of string variable.
     */
    public String getValue() {
        return value;
    }

    /**
     * This method provides a string representation of the token.
     * @return string representation of the token.
     */
    @Override
    public String toString() {
        String string = tokenType.toString();
        if (value != null)
            string = string + "(" + this.value + ")";
        return string;
    }
}
