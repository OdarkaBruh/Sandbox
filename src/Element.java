import java.util.ArrayList;

public class Element {
    private static final char[] actions = new char[]{'*', '/', '+', '-'};
    public static ArrayList<Element> elements = new ArrayList<>();

    int priority = 0;
    char actionBranch;
    Element leftElement;
    Element rightElement;
    Element parent;

    Element(String value) {
        System.out.println(value);
        elements.add(this);
        int index;
        if ((index = getHighestAction(value)) != -1) {
            actionBranch = value.charAt(index);
            rightElement = new Element(value.substring(0,index+1));
            leftElement = new Element(value.substring(index+1));
        }
    }

    private int countActions(String value) {
        int i = 0;
        for (char c : value.toCharArray()) {
            System.out.println(i);
            for (char a : actions) {
                if (a == c) {
                    i++;
                    break;
                }
            }
        }
        return i;
    }

    private void splitValue(String value) {
        int index;
        if ((index = value.lastIndexOf('('))!=-1){
            index += getHighestAction(value.substring(index, value.indexOf(')')));

            parent = new Element(value.substring(index-1, value.indexOf(')')));
        }
        //this.actionBranch = index;

    }

    private int getHighestAction(String value) {
        int index = 0;
        if ((index = value.lastIndexOf('('))!=-1){
            System.out.println("BEFORE: "+value);
            value = value.substring(index+1);
            value = value.substring(0, value.indexOf(')'));
            System.out.println("AFTER: "+value);
        }
        System.out.println("!!");
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
        return actionBranch + " " + leftElement + " " + rightElement;
    }
}



