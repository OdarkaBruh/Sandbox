import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInput {
    private static Pattern actionPattern = Pattern.compile("[+\\-/*^]+");
    private static Pattern variablePattern = Pattern.compile("[\\w$]+");
    private static Pattern digitPattern = Pattern.compile("[\\d.,]+");
    //, HashMap<String, Double> variables
    public static boolean check(String formula) {
        if (!areCharactersValid(formula)) System.err.println("Unvalid characters are present");
        else if (!areBracketsHaveSense(formula)) System.err.println("Check brackets");
        else if (!areMissingSymbolsPresent(formula)) System.err.println("Check for typos");
        else if (!areDigitsCorrect(formula)) System.err.println("1");
        else if  (!checkSymbolsInsideBrackets(formula)) System.err.println("2");
        else if (!areVariablesInHashMap(formula)) System.err.println("Variable Error!");
        else return true;
        return false;
    }

    public static boolean areCharactersValid(String input) {
        input = digitPattern.matcher(input).replaceAll("");
        input = variablePattern.matcher(input).replaceAll("");
        input = actionPattern.matcher(input).replaceAll("");
        input = input.replaceAll("[()]", "");
        System.out.println(input + " ");
        return input.isEmpty();
    }

    private static boolean areBracketsHaveSense(String input) {
        input = input.substring(input.indexOf('('), input.lastIndexOf(')')+1);
        int index = 0;
        for (char c : input.toCharArray()) {
            if (index < 0) return false;
            else if (c == '(') index++;
            else if (c == ')') index--;
        }
        return index == 0;
    }

    private enum typeChar {
        ACTION, VARIABLE, DIGIT, BRACKETS;
    }

    private static boolean areMissingSymbolsPresent(String input) {
        typeChar previousCharacter = getCharType(input.charAt(0));
        typeChar currentCharacter;
        for (int i = 1; i < input.length(); i++) {
            currentCharacter = getCharType(input.charAt(i));
            if ((currentCharacter == typeChar.DIGIT && previousCharacter == typeChar.VARIABLE) ||
                    (currentCharacter == typeChar.VARIABLE && previousCharacter == typeChar.DIGIT)) {
                System.err.println("Missed symbol between number and letters: " + input.charAt(i));
                return false;
                /*
            } else if ((currentCharacter == typeChar.BRACKETS && previousCharacter != typeChar.ACTION && previousCharacter != typeChar.BRACKETS) ||
                    (previousCharacter == typeChar.BRACKETS && currentCharacter != typeChar.ACTION && currentCharacter != typeChar.BRACKETS)) {
                System.err.println("Brackets don't have action symbol near");
                return false;*/
            } else if (currentCharacter == typeChar.ACTION && previousCharacter == typeChar.ACTION) {
                System.err.println(">1 action in a row");
                return false;
            }
            previousCharacter = currentCharacter;
        }
        return true;
    }

    private static boolean areDigitsCorrect(String input) {
        int comaCounter = 0;
        int digitCounter = 0;
        boolean previousCharacterIsDot = false;

        for (char c: input.toCharArray()){
            if (Character.isDigit(c)) {
                digitCounter++;
                previousCharacterIsDot = false;
            }
            else if (c == ',' || c == '.') {
                comaCounter++;
                if (comaCounter > 1) {
                    System.err.println("Two dots in one number.");
                    return false;
                } else if (digitCounter == 0) {
                    System.err.println("No digits before dot.");
                    return false;
                }
                previousCharacterIsDot = true;
            }
            else {
                if (previousCharacterIsDot) {
                    System.err.println("No digits after dot.");
                    return false;
                }
                comaCounter = 0;
                digitCounter = 0;
            }
        }
        return true;
    }

    public static boolean checkSymbolsInsideBrackets(String input) {
        Pattern pattern = Pattern.compile("\\((.+?)\\)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println("MATCHER: "+matcher.group(1));
            boolean actionPresent = false;
            boolean variablePresent = false;
            for (int i = 0; i < (input.indexOf('(') == -1 ? input.length() : input.indexOf('(')); i++) {
                if (getCharType(input.charAt(i)) == typeChar.ACTION) {
                    if (!(i == 0 && input.charAt(0) == '-')) {
                        actionPresent = true;
                    }
                } else variablePresent = true;
            }
            if (!variablePresent) {
                System.err.println("Only action inside brackets");
                return false;
            }
        }
        return true;
    }

    private static typeChar getCharType(char c) {
        if (Character.isDigit(c) || c == ',' || c == '.') return typeChar.DIGIT;
        else if (Character.isAlphabetic(c) || c == '$' || c == '_') return typeChar.VARIABLE;
        else if ("*-+/^".indexOf(c) != -1) return typeChar.ACTION;
        else if (c == '(' || c == ')') return typeChar.BRACKETS;

        System.err.println("Unknown type of symbol (getCharType)");
        return null;
    }

    public static boolean areVariablesInHashMap(String input) {
        int indexStart = 0;
        String value;
        for (int i = 0; i < input.length(); i++) {
            if (getCharType(input.charAt(i)) == typeChar.VARIABLE) indexStart++;
            else if (indexStart != 0) {
                value = input.substring(i-indexStart, i);
                if (!Main2.variables.containsKey(value)) {
                    System.err.println("Value " + value + "doesn't exist.");
                    return false;
                }
                indexStart = 0;
            }
        }
        return true;
    }

    private static String getSubstringInBrackets(String input) {
        System.out.println(input);
        int openBrackets = input.indexOf('(')+1;
        int closeBrackets = input.lastIndexOf(')');

        if (input.indexOf(')', openBrackets, closeBrackets) != -1)
            return input.substring(openBrackets, input.indexOf(')', openBrackets));
        else return input.substring(openBrackets,closeBrackets);
    }
}
