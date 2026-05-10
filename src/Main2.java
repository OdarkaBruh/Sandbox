import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Main2 {
    public static HashMap<String,Double> variables;
    public static void main(String[] args) {
        String n2 = "b*(3+(4*abc)*(-1)/abc)";
        HashMap<String, Double> var = new HashMap<>(){{
            put("abc", 2.0);
            put("b", 2.3);
            put("A", 4.0);
        }};

        long startTime = System.currentTimeMillis();
        calculate(n2, var);
        long endtime = System.currentTimeMillis();
        System.out.println("Time: " + (endtime - startTime));

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
            return calculateResult();
        } else return -1;
    }
    private static void createTokens(String formula) {
        if (!CacheMachine.checkCache(formula)) {
            new Token(formula,  0);
            Token.tokens.getLast().priority = -1; //for number-only;
            CacheMachine.saveCache(formula, Token.tokens);
        }
    }

    private static void getVariables() {
        for (Token t: Token.tokens) if (t.variableName != null) t.variableValue = variables.get(t.variableName);
    }

    private static double calculateResult() {
        getVariables();
        while (Token.tokens.size()!=1) {
            Token maxPriority = Token.tokens.stream().max(Comparator.comparingInt(s -> s.priority)).orElseThrow();
            maxPriority.count();
        }
        return Token.getResultAndClearTokens();
    }

    private static String formatFormula(String formula) {
        return formula.replace(" ", "").replace(',', '.')
                .replace('%', '/');
    }
}



