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

    String variableName;
    /**
     * Value of the variable. Digits are saved here.
     * The values of the variables are retrieved from Hashmap and saved here too.
     */
    double variableValue;
    /** The action (math symbol after digit). Should be unassigned (aka == 0) only for the last token */
    String actionBranch;

    /**
     * Creates token. It will cut formula to first action symbol, everything before it will parse as variable/number
     * and everything after (if something left) will pass to create a next token.
     *
     * @param value    The formula to parse (or part of it)
     * @param priority The priority of parent action (to account for brackets)
     */
    Token(String value, int priority) {
        this.priority = priority;
        tokens.add(this);

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '(') this.priority += 2;
            else if (value.charAt(i) == ')') this.priority -= 2;
            else if (i == 0 && value.charAt(0) == '-') { //if negative number in brackets (ex. (-5))
                while (!isAction(value.charAt(i + 1)) || i == value.length()) i++;
                parseVariable(value.substring(1, i));
                variableValue *= -1;
            } else if (isAction(value.charAt(i))) {
                actionBranch = String.valueOf(value.charAt(i));
                parseVariable(value.substring(0, i));
                createNextToken(value.substring(i + 1));
                break;
            }
        }

        if (actionBranch == null) parseVariable(value);
    }

    public Token(Token t) {
        this.priority = t.priority;
        this.variableValue = t.variableValue;
        this.actionBranch = t.actionBranch;
        this.variableName = t.variableName;
    }

    /**
     * Parse string before the action symbol into a variable (and gets its value) or a number.
     * <p/>
     * In rare cases, the value may be empty, for example, when the action is in between two sets of brackets:
     * ((1*a)+(2*b))
     * "+" will have an empty value. Therefore, to avoid errors, it will be assigned a value of 0 or 1,
     * depending on the action (operation).
     *
     * @param value The string to be parsed
     */
    private void parseVariable(String value) {
        value = value.replaceAll("[()]", "");
        if (value.isEmpty() && variableName == null) {
            if (actionBranch.equals("*") || actionBranch.equals("/") || actionBranch.equals("^")) variableValue = 1;
            else variableValue = 0;
        } else if (Character.isDigit(value.charAt(0))) variableValue = Double.parseDouble(value);
        else variableName = value;
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
    private void createNextToken(String formula) {
        if (!formula.replaceAll("[()]", "").isEmpty()) {
            new Token(formula, this.priority);
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
        sb.append((actionBranch == null ? ' ' : actionBranch));
        sb.append("(").append(priority).append(")   ");
        sb.append(variableValue);
        sb.append("\t").append((variableName == null ? "" : variableName));
        return sb.toString();
    }


    /**
     * Performs a calculation (action symbol). The token always contains the number to the left of the action,
     * and the action itself, but not the right number. For simplicity, it will just call method of next (right) token,
     * because one way or another, the result needs to be safe there.
     * <p>
     * Note: Right symbol will never be null, because the last token is a token with the last number (and no action)
     */
    public void count() {
        tokens.get(tokens.indexOf(this)).calculatePreviousAction(this);
        tokens.remove(this);
    }

    /**
     * Method handles the action to the left from its number
     *
     * @param prevToken the previous token which action is calculated
     */
    public void calculatePreviousAction(Token prevToken) {
        switch (prevToken.actionBranch) {
            case "*":
                variableValue = prevToken.variableValue * variableValue;
                break;
            case "/":
                variableValue = prevToken.variableValue / variableValue;
                break;
            case "+":
                variableValue = prevToken.variableValue + variableValue;
                break;
            case "-":
                variableValue = prevToken.variableValue - variableValue;
                break;
            case "^":
                variableValue = Math.pow(prevToken.variableValue, variableValue);
                break;
        }
    }

    public static double getResultAndClearTokens() {
        double result = Token.tokens.getLast().variableValue;
        System.out.println("FINISHED! Result:" + result);
        tokens.clear();
        return result;
    }

    /**
     * Prints all tokens (useful for debug)
     */
    public static void printAllTokens() {
        System.out.println();
        for (Token e : Token.tokens) {
            System.out.println(e);
        }
    }
}



