import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main2 {
    private static ArrayList<String> processed = new ArrayList<>();

    public static void main(String[] args) {
        String n1 = "1*3+4";
        String n2 = "1*(3+(4*9)/7)";
        new Element(n2, null, 0);
        for (Element e: Element.elements) {
            System.out.println(e);
        }
    }
}

