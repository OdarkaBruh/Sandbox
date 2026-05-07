import java.util.ArrayList;

/**
 * Class for creating Tokens.
 * Each token saves one action, number to the left of this action, it's priority and neighbours.
 */
public class Token {
    /** The arraylist of all (unprocessed) tokens */
    public static ArrayList<Token> tokens = new ArrayList<>();

    /**
     * The priority of this action. +2 points for each open brackets before
     * and +1 if the action should be prioritized (aka * / ^ )
     */
    int priority;
    /**
     * Value of the variable. Digits are saved here.
     * The values of the variables are retrieved from Hashmap and saved here too.
     */
    double variableValue;
    /** The action (math symbol after digit). Should be unassigned (aka == 0) only for the last token */
    char actionBranch = 0;

    /** The previous action (not by priority, by proximity) */
    Token leftToken;
    /** The next action (not by priority, by proximity) */
    Token rightToken;

    /**
     * Creates token. It will cut formula to first action symbol, everything before it will parse as variable/number
     * and everything after (if something left) will pass to create a next token.
     *
     * @param value     The formula to parse (or part of it)
     * @param leftToken The previous token
     * @param priority  The priority of parent action (to account for brackets)
     */
    Token(String value, Token leftToken, int priority) {
        if (value.replaceAll("[()]", "").isEmpty()) leftToken.rightToken = null;

        this.leftToken = leftToken;
        this.priority = priority;

        tokens.add(this);

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '(') this.priority += 2;
            else if (value.charAt(i) == ')') this.priority -= 2;
            //if negative number in brackets (ex. (-5))
            else if (i == 0 && value.charAt(0) == '-') {
                while (!isAction(value.charAt(i + 1)) || i == value.length()) i++;
                parseVariable(value.substring(1, i));
                variableValue *= -1;
            } else if (isAction(value.charAt(i))) {
                actionBranch = value.charAt(i);
                parseVariable(value.substring(0, i));
                createNewToken(value.substring(i + 1));
                break;
            }
        }

        if (actionBranch == 0) parseVariable(value);
    }

    /**
     * Parse string before the action symbol into a variable (and gets its value) or a number.
     * <p/>
     * In rare cases, the value may be empty, for example, when the action is in between two sets of brackets:
     * ((1*a)+(2*b))
     * This "+" symbol will have an empty value. Therefore, to avoid errors, it will be assigned a value of 0 or 1,
     * depending on the action (operation).
     *
     * @param value The string to be parsed
     */
    private void parseVariable(String value) {
        value = value.replaceAll("[()]", "");
        if (value.isEmpty()) {
            if (actionBranch == '*' || actionBranch == '/' || actionBranch == '^') variableValue = 1;
            else variableValue = 0;
        } else if (Character.isDigit(value.charAt(0))) variableValue = Double.parseDouble(value);
        else variableValue = Main2.variables.get(value);

    }

    /**
     * Checks whether a character is an action (* / ^ + -) and increases priority if needed
     *
     * @param a The character to be checked
     * @return whether a character is an action
     */
    private boolean isAction(char a) {
        if ((a == '*') || (a == '/') || (a == '^')) priority += 1;
        else return ((a == '+') || (a == '-'));
        return true;
    }

    /**
     * Checks whether there are more symbols after the action symbol and if yes -
     * creates a new token and passes it to them.
     *
     * @param formula The substring of formula after the action symbol
     */
    private void createNewToken(String formula) {
        if (!formula.replaceAll("[()]", "").isEmpty()) {
            rightToken = new Token(formula, this, this.priority);
        }
    }

    /**
     * Information about the class instance to be printed
     *
     * @return the String with information about instance (action symbol, priority, variableValue)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((actionBranch == 0 ? ' ' : actionBranch));
        sb.append("(").append(priority).append(")   ");
        sb.append(variableValue);
        return sb.toString();
    }


    /**
     * Performs a calculation (action symbol). The token always contains the number to the left of the action,
     * so it needs to get the second (right) number from the next (right) token. For simplicity, it will then override
     * the value of the next (right) token with this value. If left token exists, it will relink it with the right token.
     * After all of this, it will delete itself from the list of tokens.
     * <p>
     * Note: Right symbol will never be null, because the last token is a token with the last number (and no action)
     */
    public void count() {
        if (actionBranch == '*') variableValue *= rightToken.variableValue;
        else if (actionBranch == '/') variableValue /= rightToken.variableValue;
        else if (actionBranch == '+') variableValue += rightToken.variableValue;
        else if (actionBranch == '-') variableValue -= rightToken.variableValue;
        else if (actionBranch == '^') variableValue = Math.pow(variableValue, rightToken.variableValue);
        else throw new RuntimeException("(Unknown error) oh huh......");
        rightToken.variableValue = variableValue;

        if (leftToken != null) leftToken.rightToken = rightToken;
        rightToken.leftToken = leftToken;

        tokens.remove(this);
    }

    /**
     * Prints all tokens (useful for debug)
     */
    public static void printAllTokens() {
        for (Token e : Token.tokens) {
            System.out.println(e);
        }
    }
}



