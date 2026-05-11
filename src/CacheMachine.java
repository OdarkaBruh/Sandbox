import java.util.ArrayList;
import java.util.HashMap;

/**
 * Saves tokens from uncashed calls and loads tokens if the formula have been cashed
 * <p/>
 * Note: works correctly even if variable values in Hashmap were changed.
 * But if they also changed names - it won't recognize them.
 */
public class CacheMachine {
    /** All processed before formulas and their tokens (in a state before calculation) */
    private static final HashMap<String, ArrayList<Token>> processed = new HashMap<>();

    /**
     * Checks cache for this formula:
     * If it doesn't exist, returns false.
     * But if it does - loads them into TokenManager.tokens and returns true
     *
     * @param formula formula to check in the cache
     * @return was the formula found in the cache
     *
     */
    public static boolean checkCache(String formula) {
        if (!processed.containsKey(formula)) return false;
        for (Token t : processed.get(formula)) TokenManager.tokens.add(new Token(t));
        return true;
    }

    /**
     * Saves formula in the cache
     *
     * @param formula   the formula which was parsed
     * @param arrayList list of created tokens
     */
    public static void saveCache(String formula, ArrayList<Token> arrayList) {
        ArrayList<Token> copy = new ArrayList<>();
        for (Token t : arrayList) copy.add(new Token(t));
        processed.put(formula, copy);
    }

    /**
     * checks if formula is inside cache (was processed before)
     *
     * @param formula the formula to check
     * @return the result of search (was found or not)
     */
    public static boolean formulaInCache(String formula) {
        return processed.containsKey(formula);
    }
}
