public class Token {
    public String value;
    public StringBuilder rightPart = new StringBuilder();

    public int priority = 0;

    public Token(String value){
        this.value = value;
    }

    public Token(Character value){
        this.value = String.valueOf(value);
    }

    public Token(String value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public void addRightPart(Character newPart, int priority) {
        rightPart.append(newPart);
        this.priority += priority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value).append("(").append(priority).append(")         ").append(rightPart);
        return sb.toString();
    }

    public int getPriority() {
        return priority;
    }
}
