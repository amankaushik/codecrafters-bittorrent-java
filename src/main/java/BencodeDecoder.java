import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BencodeDecoder {

    static List<String> decode(String input) {
        if (input.length() < 3) {
            return Collections.emptyList();
        }

        var result = new ArrayList<String>();
        var totalLength = input.length();

        for (int i = 0; i < totalLength; i++) {
            if (input.charAt(i) == 'l') {
                // System.out.println("Encountered 'l', skipping iteration");
                totalLength--;
                continue;
            } else if (input.charAt(i) == 'i') {
                var ch = input.charAt(i);
                var workingInput = new StringBuilder();

                while (true && ch != 'e') {
                    workingInput.append(ch);
                    i++;
                    ch = input.charAt(i);
                }

                workingInput.append(ch);
                // System.out.println(workingInput);
                result.add(IntegerDecoder.decode(workingInput.toString()));
                // System.out.println(result);

            } else if (Character.isDigit(input.charAt(i))) {
                // System.out.println(i);;
                var expectedLength = Integer.parseInt(input.substring(i, i + 1));
                i++;
                // System.out.println(i);;
                var colonChar = input.charAt(i);

                if (colonChar != ':') {
                    throw new RuntimeException("Expected the char to be ':'");
                }

                if (i + expectedLength > input.length()) {
                    throw new RuntimeException("Working input cannot be greater than provided input");
                }

                i++;
                // System.out.println(i);;
                // System.out.println(expectedLength);
                // System.out.println(i + expectedLength);
                var workingInput = input.substring(i, i + expectedLength);
                i = i + expectedLength - 1;
                // System.out.println(i);;
                // System.out.println(workingInput);
                result.add(StringDecoder.decode(workingInput));
                // System.out.println(result);
            } else {
                throw new RuntimeException("Unexpected character at the beginning of the input");
            }
        }
        return result;
    }
}
