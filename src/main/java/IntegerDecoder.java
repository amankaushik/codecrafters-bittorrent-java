public class IntegerDecoder {

    public static String decode(String input) {
        return extractInput(input);
    }

    static String extractInput(String input) {

        if (input.charAt(0) != 'i') {
            throw new RuntimeException("Integer input doesn't start with 'i'");
        }

        int index = 1;
        StringBuilder stringBuilder = new StringBuilder();

        while (true && index < input.length()) {
            var ch = input.charAt(index);
            if (ch == 'e') {
                break;
            }
            stringBuilder.append(ch);
            index++;
        }

        return stringBuilder.toString();
    }
}
