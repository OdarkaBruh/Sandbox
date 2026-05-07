import java.util.ArrayList;
import java.util.HashMap;

public class Token {
    public static ArrayList<Token> tokens = new ArrayList<>();

    int priority;
    double variableValue;
    char actionBranch = 0;

    Token leftToken;
    Token rightToken;

    Token(String value, Token leftToken, int priority) {
        if (value.replaceAll("[()]", "").isEmpty()) leftToken.rightToken = null;

        this.leftToken = leftToken;
        this.priority = priority;

        tokens.add(this);

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '(') this.priority += 2;
            else if (value.charAt(i) == ')') this.priority -= 2;
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

    private void parseVariable(String value) {
        value = value.replaceAll("[()]", "");
        System.out.println(value);
        if (value.length() == 0) {
            if (actionBranch == '*' || actionBranch == '/' || actionBranch == '^') variableValue = 1;
            else variableValue = 0;

        }
        else if (Character.isDigit(value.charAt(0))) variableValue = Double.parseDouble(value);
        else variableValue = Main2.variables.get(value);

    }

    private boolean isAction(char a) {
        if (a == '*') priority += 1;
        else if (a == '/') priority += 1;
        else return (a == '+') || (a == '-');
        return true;
    }

    private void createNewToken(String formula) {
        if (!formula.replaceAll("[()]", "").isEmpty()) {
            //System.out.println(formula);
            rightToken = new Token(formula, this, this.priority);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((actionBranch == 0 ? ' ' : actionBranch));
        sb.append("(").append(priority).append(")   ");
        sb.append(variableValue);
        return sb.toString();
    }


    public void count() {
        if (actionBranch == '*') variableValue *= rightToken.variableValue;
        else if (actionBranch == '/') variableValue /= rightToken.variableValue;
        else if (actionBranch == '+') variableValue += rightToken.variableValue;
        else if (actionBranch == '-') variableValue -= rightToken.variableValue;
        else throw new RuntimeException("oh huh......");
        rightToken.variableValue = variableValue;

        if (leftToken != null) leftToken.rightToken = rightToken;
        rightToken.leftToken = leftToken; //never null, because there will be at least 1 with value-only

        tokens.remove(this);

    }

    public static void printAllTokens() {
        for (Token e : Token.tokens) {
            System.out.println(e);
        }
    }
}



