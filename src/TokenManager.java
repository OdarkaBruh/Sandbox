import java.util.ArrayList;
import java.util.Comparator;

public class TokenManager {
    /** The arraylist of all (unprocessed) tokens */
    public static ArrayList<Token> tokens = new ArrayList<>();

    public enum modifiers {
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


    static double calculateResult() {
        for (Token t: Token.tokens) t.applyModifiers();

        while (Token.tokens.size()!=1) {
            //printAllTokens();
            Token maxPriority = Token.tokens.stream().max(Comparator.comparingInt(s -> s.priority)).orElseThrow();
            maxPriority.count();
        }
        return TokenManager.getResultAndClearTokens();
    }



    public static double getResultAndClearTokens() {
        double result = Token.tokens.getLast().variableValue;
        System.out.println("FINISHED! Result:" + result);
        Token.tokens.clear();
        return result;
    }

    /**
     * Prints all tokens (useful for debug)
     */
    public static void printAllTokens() {
        System.out.println();
        for (Token e : Token.tokens) System.out.println(e);
    }
}
