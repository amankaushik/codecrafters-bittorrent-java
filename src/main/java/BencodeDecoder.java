import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BencodeDecoder {

    static List<Object> decode(String input) {
        // System.out.println("Input: " + input);
        if (input.length() < 3) {
            return Collections.emptyList();
        }
        var result = new ArrayList<Object>();
        var totalLength = input.length();

        for (int i = 0; i < totalLength; i++) {
            if (input.charAt(i) == 'l') {
                // System.out.println("Encountered 'l', skipping iteration");
                var closingIndex = totalLength-1;
                var ch = input.charAt(closingIndex);

                while (true && ch != 'e') {
                    closingIndex--;
                }
                result.add(decode(input.substring(i + 1, closingIndex)));
                i = closingIndex;
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
                var expectedLengthString = new StringBuilder();
                var ch = input.charAt(i);
                while (ch != ':') {
                    expectedLengthString.append(ch);
                    i++;
                    ch = input.charAt(i);
                }
                
                var expectedLength = Integer.parseInt(expectedLengthString.toString());
                var colonChar = input.charAt(i);

                if (colonChar != ':') {
                    throw new RuntimeException("Expected the char to be ':'");
                }

                if (i + expectedLength > input.length()) {
                    throw new RuntimeException("Working input cannot be greater than provided input");
                }

                i++;
                var workingInput = input.substring(i, i + expectedLength);
                i = i + expectedLength - 1;
                result.add(StringDecoder.decode(workingInput));
            } else {
                throw new RuntimeException("Unexpected character at the beginning of the input");
            }
        }
        return result;
    }
}
