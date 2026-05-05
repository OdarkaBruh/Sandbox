import java.util.ArrayList;

public class Main3 {
    private static ArrayList<String> processed = new ArrayList<>();

    public static void main(String[] args) {
        String n = "1*(3+4*7)/8";

        for (char c : n.toCharArray()) {
            processed.add(String.valueOf(c));
        }

        System.out.println(processed);
        int index;
        while (processed.contains("(")) {
            int indexOpen = processed.lastIndexOf("(");
            int indexClosed = processed.indexOf(")");

            processed.set(indexOpen, count(indexOpen+1, indexClosed));
            for (int i = indexOpen+1; i <= indexClosed; i++) processed.remove(indexOpen+1);
        }
        count(0, processed.size());


        //System.out.println(processed);
    }

    private static String count(int indexFrom, int indexTo) {
        ArrayList<String> sub = new ArrayList<>(processed.subList(indexFrom, indexTo));
        System.out.println(sub.size() + " SUB: " + sub);
        int index;
        System.out.println(sub);
        while ((index = sub.lastIndexOf("*"))!=-1) countChar('*', sub);
        System.out.println(sub);
        while ((index = sub.lastIndexOf("/"))!=-1) countChar('/', sub);
        System.out.println(sub);
        while ((index = sub.lastIndexOf("+"))!=-1) countChar('+', sub);
        System.out.println(sub);
        while ((index = sub.lastIndexOf("-"))!=-1) countChar('-', sub);
        System.out.println(sub);
        return sub.get(0);
    }
    private static void countChar(Character character, ArrayList<String> sub){
        int index;
        while ((index = sub.lastIndexOf(String.valueOf(character)))!=-1) {
            sub.set(index-1, sub.get(index-1)+sub.get(index+1));
            sub.remove(index);
            sub.remove(index);
        }
    }

}

