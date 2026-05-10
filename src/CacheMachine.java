import java.util.ArrayList;
import java.util.HashMap;

public class CacheMachine {
    private static HashMap<String, ArrayList<Token>> processed = new HashMap<>();

    public static boolean checkCache(String formula) {
        if (!processed.containsKey(formula)) return false;

        System.out.println("Loaded!");
        for (Token t: processed.get(formula)) {
            Token.tokens.add(new Token(t));
        }
        return true;
    }

    public static void saveCache(String formula, ArrayList<Token> arrayList) {
        ArrayList<Token> copy = new ArrayList<>();
        for (Token t: arrayList) {
            copy.add(new Token(t));
        }
        processed.put(formula, copy);
        System.out.println("Saved!");
    }

    public static boolean formulaInCache(String formula) {
        return processed.containsKey(formula);
    }
}
