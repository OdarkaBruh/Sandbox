import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** Checks formula for an error */
public class CheckInput {
    /** Pattern for all actions (math symbols) */
    private static final Pattern actionPattern = Pattern.compile("[+\\-/*^]+");
    /** Pattern for all possible symbols in variables (All letters + underscore + dollar sign) */
    private static final Pattern variablePattern = Pattern.compile("[\\w$_]+");
    /** Pattern for all possible symbols in numbers (All digits + .) */
    private static final Pattern numberPattern = Pattern.compile("[\\d.]+");

    private static HashSet<Integer> additionalActionsFound;
    /**
     * Main method which is calling all sub-checks
     * Note: Hashmap will be taken from Main's variables.
     *
     * @param formula The String formula to be checked.
     * @return whether ALL checks were successful. If at least one of them failed => this check also failed
     */
    public static boolean check(String formula) {
        additionalActionsFound = getAdditionalActions(formula);

        if (!areCharactersValid(formula)) System.err.println("Invalid characters are present.\n" +
                "Please, check if you are using right mathematical symbols and variable names are spelled correctly");
        else if (!areBracketsHaveSense(formula)) System.err.println("Brackets error.\n" +
                "One of the brackets wasn't closed properly.");
        else if (!areMissingSymbolsPresent(formula)) System.err.println("Please, check the text for typos");
            //The checks below will output detailed information about the error within the method, not here
        else if (!areDigitsCorrect(formula)) System.err.println("Number-related error. More details are above.");
        else if (!checkContentInBrackets(formula)) System.err.println("Error related to parentheses (brackets)");
        else if (!areVariablesInHashMap(formula)) System.err.println("Variable doesn't exist.");
        else return true;
        return false;
    }

    /**
     * Checks for prohibited character.
     * Allowed mathematical symbols: + - * \/ ^
     * Allowed variable symbols: A-Z a-z $ _
     * Allowed digit symbols: 0-9 .
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    public static boolean areCharactersValid(String input) {
        input = numberPattern.matcher(input).replaceAll("");
        input = variablePattern.matcher(input).replaceAll("");
        input = actionPattern.matcher(input).replaceAll("");
        input = input.replaceAll("[()]", "");
        return input.isEmpty();
    }

    /**
     * Checks whether the brackets are closed correctly.
     * Reminder for future me: (<a href="https://www.geeksforgeeks.org/dsa/check-for-balanced-parentheses-in-an-expression/">fancy stack method</a>)
     * (it will be overkill here, I don't see any benefits of using it here)
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    private static boolean areBracketsHaveSense(String input) {
        input = input.substring(input.indexOf('('), input.lastIndexOf(')') + 1);
        int index = 0;
        for (char c : input.toCharArray()) {
            if (index < 0) return false;
            else if (c == '(') index++;
            else if (c == ')') index--;
        }
        return index == 0;
    }

    private enum typeChar {
        ACTION, VARIABLE, DIGIT, BRACKETS
    }

    /**
     * Checks whether:
     * 1) Digits and variables doesn't collide (ex. "4height" or "height4")
     * Yes, it's usually implied that there will be multiplication, but don't care.
     * 2) Two math symbols in a row (ex. 4++3)
     * <p/>
     * Ignores brackets EXCEPT when it's negative number (ex. (-4));
     * In other words, will throw an error for (4+(+3));
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    private static boolean areMissingSymbolsPresent(String input) {
        typeChar previousCharacter = getCharType(input.charAt(0));
        typeChar currentCharacter;

        for (int i = 1; i < input.length(); i++) {
            if (additionalActionsFound.contains(i)) {
                i = skipAdditionalAction(i, input);
                previousCharacter = typeChar.VARIABLE;
            }
            else if (input.charAt(i) != '(' && input.charAt(i) != ')') {
                currentCharacter = getCharType(input.charAt(i));
                if ((currentCharacter == typeChar.DIGIT && previousCharacter == typeChar.VARIABLE) ||
                        (currentCharacter == typeChar.VARIABLE && previousCharacter == typeChar.DIGIT)) {
                    System.err.println(input.charAt(i));
                    System.err.println(additionalActionsFound);
                    System.err.println("There is no math symbol between the number and the letter: " + input.charAt(i));
                    return false;
                } else if ((currentCharacter == typeChar.ACTION && previousCharacter == typeChar.ACTION) &&
                        !(input.charAt(i) == '-' && input.charAt(i - 1) == '(')) {
                    System.err.println("Two mathematical symbols: " + input.charAt(i - 1) + input.charAt(i));
                    return false;
                }
                previousCharacter = currentCharacter;
            }
        }
        return true;
    }

    private static HashSet<Integer> getAdditionalActions(String input) {
        HashSet<Integer> result = new HashSet<>();

        for (String m: Stream.of(modifiers.values()).map(modifiers::name).toArray(String[]::new)) {
            int i = 0;
            while ((i = input.indexOf(m.toLowerCase(), i+1)) != -1) result.add(i);
        }
        return result;
    }

    private static int skipAdditionalAction(int i, String input) {
        do i++;
        while (input.charAt(i) != ')' || getCharType(input.charAt(i)) == typeChar.ACTION);
        return i;
    }

    /**
     * Checks whether:
     * 1) There is at least one digit before the decimal point.
     * 2) There is at least one digit after the decimal point.
     * 3) There are 1 decimal point (dot) or less.
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    private static boolean areDigitsCorrect(String input) {
        int comaCounter = 0;
        int digitCounter = 0;
        boolean previousCharacterIsDot = false;

        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCounter++;
                previousCharacterIsDot = false;
            } else if (c == '.') {
                comaCounter++;
                if (comaCounter > 1) {
                    System.err.println("Two dots in one number.");
                    return false;
                } else if (digitCounter == 0) {
                    System.err.println("There are no digits before the decimal point.");
                    return false;
                }
                previousCharacterIsDot = true;
            } else {
                if (previousCharacterIsDot) {
                    System.err.println("There are no digits after the decimal point.");
                    return false;
                }
                comaCounter = 0;
                digitCounter = 0;
            }
        }
        return true;
    }

    /**
     * The parent method which extracts the content from the brackets and passes it for the inspection.
     * If the check is successful, this content and the brackets are replaced with "x".
     * Repeats until there are no brackets left.
     * <p/>
     * Off-topic, but: I didn't find a good way to handle extracting the contents from the brackets without
     * replacement / saving data. One way or another, it will either not work properly in examples like
     * (x+(1+y)) or ((x+1)*(x+1)). But this method works really well with all of them :D
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    public static boolean checkContentInBrackets(String input) {
        StringBuilder scannedText = new StringBuilder(input);
        int indexOpenBrackets;
        int indexClosedBrackets;

        while (scannedText.indexOf("(") != -1) {
            indexOpenBrackets = scannedText.lastIndexOf("(") + 1;
            indexClosedBrackets = scannedText.indexOf(")", indexOpenBrackets);
            if (!checkTextInBracketsForNumberOrVariable(scannedText.substring(indexOpenBrackets, indexClosedBrackets)))
                return false;
            scannedText.replace(indexOpenBrackets - 1, indexClosedBrackets + 1, "x");
        }
        return true;
    }

    /**
     * Whether variable / digit is present (ex. not "(*)")
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    private static boolean checkTextInBracketsForNumberOrVariable(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (getCharType(input.charAt(i)) != typeChar.ACTION) return true;
        }
        System.err.println("Only action symbol is inside brackets");
        return false;
    }

    /**
     * Gets character type (digit, variable or action (math symbol)).
     * Easier to understand and work with than having to check every time.
     *
     * @param c The character to be checked.
     * @return The character's type
     */
    private static typeChar getCharType(char c) {
        if (Character.isDigit(c) || c == ',' || c == '.') return typeChar.DIGIT;
        else if (Character.isAlphabetic(c) || c == '$' || c == '_') return typeChar.VARIABLE;
        else if ("*-+/^".indexOf(c) != -1) return typeChar.ACTION;
        else if (c == '(' || c == ')') return typeChar.BRACKETS;

        System.err.println("Unknown type of symbol (getCharType): " + c);
        return null;
    }

    /**
     * Checks whether variables exist in provided HashMap.
     *
     * @param input The string to be checked.
     * @return Whether the check was successful.
     */
    public static boolean areVariablesInHashMap(String input) {
        int indexStart = 0;
        String value;
        for (int i = 0; i < input.length(); i++) {
            if (additionalActionsFound.contains(i)) i = skipAdditionalAction(i, input);
            else if (getCharType(input.charAt(i)) == typeChar.VARIABLE) indexStart++;
            else if (indexStart != 0) {
                value = input.substring(i - indexStart, i);
                if (!Main2.variables.containsKey(value)) {
                    System.err.println("Variable " + value + " doesn't exist in provided Hashmap.");
                    return false;
                }
                indexStart = 0;
            }
        }
        return true;
    }
}
