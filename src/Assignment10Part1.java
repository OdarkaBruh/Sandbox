import java.util.HashMap;

/**
 * CS-B: Simple calculator
 * <p>
 * Note: Do you see in a task 10 any note about brackets? Do you see "You don't need to implement brackets yes"?
 * ...Do you see a problem?
 * ...Yes, I thought that it's part of the task.
 */
public class Assignment10Part1 {
    /** for easy switch between methods in a switch */
    private static final int MODE = 0;

    /** overriding main to call calculate if formula and hashmap exists */
    public static void main(String formula, HashMap<String, Double> variables) {
        TokenManager.calculate(formula, variables);
    }

    /**
     * the main method which creates formula and hashmap (depending on the selected mode) and passes them to calculate;
     *
     * @param args
     */
    public static void main(String[] args) {
        switch (MODE) {
            case 0 -> createNewValuesAndCall();
            case 1 -> compareTimeWithoutAndWithCache();
            case 2 -> compareTimeWithoutAndWithCacheAverage();
        }
    }

    /** Creates custom values and custom formula (can paste yours here) and then calls calculate */
    private static void createNewValuesAndCall() {
        String formula = "c * 4 + 5.4 / a ^ b ";
        HashMap<String, Double> variables = new HashMap<>() {{
            put("a", 1.0);
            put("b", 2.3);
            put("c", 4.0);
        }};
        System.out.println("Result: " + TokenManager.calculate(formula, variables));
    }

    /** Traces the speed of calculation without cache (first call) and with cache (second call) */
    private static void compareTimeWithoutAndWithCache() {
        String formula = "a + b * c - 40 ^ a ";
        HashMap<String, Double> variables = new HashMap<>() {{
            put("a", 1.0);
            put("b", 2.3);
            put("c", 4.0);
        }};
        long startTime = System.currentTimeMillis();
        TokenManager.calculate(formula, variables);
        long endtime = System.currentTimeMillis();
        System.out.println("Time without cache: " + (endtime - startTime) + "\n");

        startTime = System.currentTimeMillis();
        TokenManager.calculate(formula, variables);
        endtime = System.currentTimeMillis();
        System.out.println("Time with cache: " + (endtime - startTime) + "\n");
    }

    /** Traces the speed of 10.000 calculation without cache and with cache */
    private static void compareTimeWithoutAndWithCacheAverage() {
        int doTimes = 10_000;
        String formula = "a + b * c - 40 ^ a ";
        HashMap<String, Double> variables = new HashMap<>() {{
            put("a", 1.0);
            put("b", 2.3);
            put("c", 4.0);
        }};

        TokenManager.useCache = false;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < doTimes; i++) TokenManager.calculate(formula, variables);
        long endtime = System.currentTimeMillis();
        float time1 = endtime - startTime;
        System.out.printf("\nTime without cache (average): %.5f", (time1 / doTimes));

        TokenManager.useCache = true;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < doTimes; i++) TokenManager.calculate(formula, variables);
        endtime = System.currentTimeMillis();
        float time2 = endtime - startTime;
        System.out.printf("\nTime with cache (average): %.5f", (time2 / doTimes));
        System.out.printf("\n%.2f%% faster", time2 * 100 / time1);
    }
}