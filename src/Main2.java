import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Main2 {
    private static HashMap<String, Token> processed = new HashMap<>();
    public static HashMap<String,Double> variables;
    public static void main(String[] args) {
        String n2 = "b*(3+(4*abc)*(-1)/abc)";
        HashMap<String, Double> var = new HashMap<>(){{
            put("abc", 2.0);
            put("b", 2.3);
            put("A", 4.0);
        }};
        calculate(n2, var);
        //System.out.println("VALID: "+CheckInput.check("a*(3,17/(1+$dcx))"));
    }

    public static void main(String formula, HashMap<String,Double> variables) {
        calculate(formula, variables);

    }

    public static double calculate(String formula, HashMap<String,Double> variables) {
        Main2.variables = variables;
        createTokens(formula);
        if (CheckInput.check(formula)) {
            System.out.println("Check successful!");
            while (Token.tokens.size()!=1) {
                System.out.println();
                Token.printAllTokens();
                Token maxPriority = Token.tokens.stream().max(Comparator.comparingInt(s -> s.priority)).orElseThrow();
                maxPriority.count();
            }
            System.out.println("FINISHED! "+Token.tokens.getLast().variableValue);
            // TODO:
            return Token.tokens.getLast().variableValue;
        } else return -1;
    }
    private static void createTokens(String formula) {
        new Token(formatFormula(formula), null, 0);
        Token.tokens.getLast().priority = -1; //for number-only;
    }

    private static String formatFormula(String formula) {
        return formula.replace(" ", "");
    }
}



