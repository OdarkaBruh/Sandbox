import java.util.ArrayList;
import java.util.Objects;

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
    ArrayList<modifiers> valueModifier = new ArrayList<>();

    /**
     * Creates token. It will cut formula to first action symbol, everything before it will parse as variable/number
     * and everything after (if something left) will pass to create a next token.
     *
     * @param formula  The formula to parse (or part of it)
     * @param priority The priority of parent action (to account for brackets)
     */
    Token(String formula, int priority) {
        tokens.add(this);
        this.priority = priority;

        //formula = removeBracketsFromString(formula);
        int j = 0;
        while (formula.charAt(j) == '(') j++;

        for (int i = j; i < formula.length(); i++) {
            if (formula.charAt(i) == '(') {
                valueModifier.add(modifiers.getByName(formula.substring(j, i)));
                this.priority += 2;
                j = i + 1;
            } else if (formula.charAt(i) == ')') {
                this.priority -= 2;
            } else if (i == j && formula.charAt(j) == '-') {
                valueModifier.add(modifiers.NEGATIVE);
                j++;
            } else if (isAction(formula.charAt(i))) {
                actionBranch = String.valueOf(formula.charAt(i));
                if (i == j) setDefaultVariableValue(actionBranch);
                else parseVariable(formula.substring(j, i));

                createNextToken(formula.substring(i + 1));
                break;
            }
        }
        if (this.actionBranch == null) parseVariable(formula.substring(j)); //the last token
    }

    private String removeBracketsFromString(String value) {
        int i = 0;
        while (value.charAt(i) == '(') {
            this.priority += 2;
            i++;
        }
        return value.substring(i);
    }

    private void setDefaultVariableValue(String actionBranch) {
        if (actionBranch.equals("*") || actionBranch.equals("/") || actionBranch.equals("^"))
            variableValue = 1;
        else variableValue = 0;
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
        if (Character.isDigit(value.charAt(0))) {
            variableValue = Double.parseDouble(value);
        } else variableName = value;
    }

    public Token(Token t) {
        this.priority = t.priority;
        this.variableValue = t.variableValue;
        this.actionBranch = t.actionBranch;
        this.variableName = t.variableName;
        this.valueModifier = t.valueModifier;
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
     * Performs a calculation (action symbol). The token always contains the number to the left of the action,
     * and the action itself, but not the right number. For simplicity, it will just call method of next (right) token,
     * because one way or another, the result needs to be safe there.
     * <p>
     * Note: Right symbol will never be null, because the last token is a token with the last number (and no action)
     */
    public void count() {
        tokens.get(tokens.indexOf(this) + 1).calculatePreviousAction(this);
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
                this.variableValue = prevToken.variableValue * this.variableValue;
                break;
            case "/":
                this.variableValue = prevToken.variableValue / this.variableValue;
                break;
            case "+":
                this.variableValue = prevToken.variableValue + this.variableValue;
                break;
            case "-":
                this.variableValue = prevToken.variableValue - this.variableValue;
                break;
            case "^":
                this.variableValue = Math.pow(prevToken.variableValue, this.variableValue);
                break;
        }
    }

    public void applyModifiers() {
        if (this.variableName != null && !variableName.isEmpty()) variableValue = Main2.variables.get(variableName);
        for (modifiers m : valueModifier) {
            switch (m) {
                case modifiers.NEGATIVE -> variableValue = -variableValue;
                case modifiers.SIN -> variableValue = Math.sin(variableValue);
                case modifiers.COS -> variableValue = Math.cos(variableValue);
                case modifiers.TAN -> variableValue = Math.tan(variableValue);
                case modifiers.ATAN -> variableValue = Math.atan(variableValue);
                case modifiers.LOG2 -> variableValue = Math.log(variableValue) / Math.log(2);
                case modifiers.LOG10 -> variableValue = Math.log10(variableValue);
                case modifiers.SQRT -> variableValue = Math.sqrt(variableValue);
            }
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
        sb.append("\t(").append(String.format("%-2s",priority)).append(")   ");
        sb.append(String.format("%-10.2f", variableValue)).append("\t");
        //sb.append(String.format("%-10s \t", variableName == null ? "" : variableName));
        if (!valueModifier.isEmpty()) {
            sb.append("[ ");
            valueModifier.forEach(n -> sb.append(n).append(" "));
            sb.append("]\t");
        }
        return sb.toString();
    }
}

enum modifiers {
    NEGATIVE("neg"),
    SIN("sin"),
    COS("cos"),
    TAN("tan"),
    ATAN("atan"),
    LOG10("log10"),
    LOG2("log2"),
    SQRT("sqrt");

    private final String name;

    modifiers(String s) {
        name = s;
    }

    public static modifiers getByName(String s) {
        for (modifiers m : modifiers.values()) {
            if (m.name.equals(s)) return m;
        }
        return null;
    }
}

