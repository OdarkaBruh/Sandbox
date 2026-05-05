import java.util.ArrayList;
import java.util.stream.IntStream;

public class Element {
    private static final char[] actions = new char[]{'*', '/', '+', '-'};
    public static ArrayList<Element> elements = new ArrayList<>();

    int priority;
    int element;
    char actionBranch = 0;

    Element leftElement;
    Element rightElement;
    Element parent;

    Element(String value, Element leftElement, int priority) {
        System.out.println("V: "+ value);
        this.leftElement = leftElement;
        this.priority = priority;

        elements.add(this);

        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '(') this.priority+=2;
            else if (value.charAt(i) == ')') this.priority-=2;
            else if (Character.isDigit(value.charAt(i))) element = value.charAt(i);
            else if (value.charAt(i) == ' ') continue;
            else {
                actionBranch = value.charAt(i);
                rightElement = new Element(value.substring(i+1), this, this.priority);
                break;
            }
        }
    }

    private int getHighestAction(String value) {
        int index = 0;
        if ((index = value.lastIndexOf('('))!=-1){
            value = value.substring(index+1);
            value = value.substring(0, value.indexOf(')'));
        }
        for (char c: actions) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == c) {
                    System.out.println(i + index);
                    return i+index;
                }
            }
        }
        System.out.println(-1);
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (actionBranch != 0) sb.append(actionBranch);
        sb.append(" (");
        sb.append(priority).append(")      : " );
        //if (leftElement != null) sb.append(leftElement);
        //sb.append("   |   ");
        return sb.toString();
    }

    private boolean isAction(char check) {
        for (char c: actions) {
            if (check == c) return true;
        }
        return false;
    }
}



