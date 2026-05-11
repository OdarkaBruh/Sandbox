import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/** Manages tokens and calculation */
public class TokenManager {
    /** The arraylist of all (unprocessed) tokens */
    public static ArrayList<Token> tokens = new ArrayList<>();
    /** Provided variables and their values */
    public static HashMap<String, Double> variables;
    /**
     * DEBUG VALUE: use CacheMachine or not (if false, formula will be checked and
     * tokens are created from scratch each time)
     */
    public static boolean useCache = true;

    /**
     * Main calculate method. It sets needed variables, calls to create and calculate tokens.
     *
     * @param formula   formula to parse
     * @param variables Hashmap with variables and their values
     * @return the result of calculation
     */
    public static double calculate(String formula, HashMap<String, Double> variables) {
        TokenManager.variables = variables;
        TokenManager.tokens.clear();

        formula = formatFormula(formula);
        if ((useCache && CacheMachine.formulaInCache(formula)) || CheckInput.check(formula)) {
            createTokens(formula);
            return TokenManager.calculateResult();
        } else return -1;
    }

    /**
     * Calls to check the cache for tokens. If it false, starts a loop of token creation and later
     * passes the result to the cache
     */
    private static void createTokens(String formula) {
        if (!useCache || !CacheMachine.checkCache(formula)) {
            new Token(formula, 0);
            CacheMachine.saveCache(formula, TokenManager.tokens);
        }
    }

    /**
     * Unifies received formula. All spaces are removed, decimal comas are swapped to points and division symbol % is
     * replaced with the correct /
     *
     * @param formula the formula to change
     * @return Unified formula
     */
    private static String formatFormula(String formula) {
        return formula.replace(" ", "").replace(',', '.')
                .replace('%', '/');
    }

    /**
     * Returns next token and removes the one which called from the list
     *
     * @param t the token which calls
     * @return the next token
     */
    static Token getNextToken(Token t) {
        Token result = tokens.get(tokens.indexOf(t) + 1);
        tokens.remove(t);
        return result;
    }

    /**
     * Calls all methods to calculate result
     *
     * @return result of the formula
     */
    static double calculateResult() {
        for (Token t : tokens) t.applyModifiers();

        while (tokens.size() != 1) {
            Token maxPriority = tokens.stream().max(Comparator.comparingInt(s -> s.priority)).orElseThrow();
            maxPriority.count();
        }

        return tokens.getLast().variableValue;
    }

    /** Prints all tokens (useful for debug) */
    public static void printAllTokens() {
        System.out.println();
        for (Token e : tokens) System.out.println(e);
    }
}
