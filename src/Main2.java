import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Main2 {
    public static HashMap<String,Double> variables;
    public static void main(String[] args) {
        String n2 = "b*(tan(3)+(4*abc)*(-7)/log10(abc))";
        String n1 = "((7+1)*sin(cos(30)))+7";
        HashMap<String, Double> var = new HashMap<>(){{
            put("abc", 2.0);
            put("b", 2.3);
            put("A", 4.0);
        }};

        long startTime = System.currentTimeMillis();
        calculate(n2, var);
        long endtime = System.currentTimeMillis();
        System.out.println("Time: " + (endtime - startTime)+"\n");

        startTime = System.currentTimeMillis();
        calculate(n2, var);
        endtime = System.currentTimeMillis();
        System.out.println("Time: " + (endtime - startTime));
    }

    public static void main(String formula, HashMap<String,Double> variables) {
        calculate(formula, variables);

    }

    public static double calculate(String formula, HashMap<String,Double> variables) {
        Main2.variables = variables;
        formula = formatFormula(formula);
        if (CacheMachine.formulaInCache(formula) || CheckInput.check(formula)) {
            createTokens(formula);
            return TokenManager.calculateResult();
        } else return -1;
    }
    private static void createTokens(String formula) {
        if (!CacheMachine.checkCache(formula)) {
            new Token(formula,  0);
            Token.tokens.getLast().priority = -1; //for number-only;
            CacheMachine.saveCache(formula, Token.tokens);
        }
    }

    private static String formatFormula(String formula) {
        return formula.replace(" ", "").replace(',', '.')
                .replace('%', '/');
    }
}



