import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static ArrayList<Token> processed = new ArrayList<>();

    public static void main(String[] args) {
        String n = "1*3+4";
        createTokens(n);
        for (Token t: processed) {
            int min = processed.stream().map(o -> o.priority).min(Integer::compare).orElseThrow();

        }
    }

    private static void createTokens(String n) {
        HashMap<Character, Integer> actions = new HashMap<>() {{
            put('+', 1);
            put('-', 1);
            put('*', 2);
            put('/', 2);
        }};
        Token prevToken = null;
        for (int i = 0; i < n.length(); i++) {
            char a = n.charAt(i);
            if (Character.isDigit(a)){
                if (prevToken != null) processed.add(prevToken);
                prevToken = new Token(a);
            } else if (actions.containsKey(a)) {
                prevToken.addRightPart(a, actions.get(a));
            }
        }
        processed.add(prevToken);

        System.out.println(processed.toString());
    }

}

