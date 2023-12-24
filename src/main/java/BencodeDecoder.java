import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class BencodeDecoder {
    static List<Object> result = new ArrayList<>();

    static int findClosingIndex(String input, int startIndex) {
        var ch = input.charAt(startIndex);
        var closingIndex = startIndex;
        while (true && ch != 'e') {
            closingIndex++;
            ch = input.charAt(closingIndex);
        }
        return closingIndex;
    }

    static DecoderResult _decode(String input) {
        // System.out.println("Input: " + input);

        if (input.length() < 3) {
            return new DecoderResult(Collections.emptyList(), 0);
        }

        var result = new ArrayList<Object>();
        var totalLength = input.length();
        int i = 0;

        for (; i < totalLength; i++) {
            if (input.charAt(i) == 'l') {
                var op = _decode(input.substring(i + 1));
                result.add(op.result);
                i = op.nextIndex;
                // System.out.println("i: " + i);
            } else if (input.charAt(i) == 'i') {
                var ch = input.charAt(i);
                var workingInput = new StringBuilder();

                while (true && ch != 'e') {
                    workingInput.append(ch);
                    i++;
                    ch = input.charAt(i);
                }

                workingInput.append(ch);
                result.add(IntegerDecoder.decode(workingInput.toString()));

            } else if (Character.isDigit(input.charAt(i))) {
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
            } else if (input.charAt(i) == 'e') {
                return new DecoderResult(result, i + 1);
            }
             else {
                throw new RuntimeException("Unexpected character at the beginning of the input");
            }
        }
        return new DecoderResult(result, i + 1);
    }

    static List<Object> _decodeList(String input) {

        return Collections.emptyList();
    }

    static List<Object> decode(String input) {
        // System.out.println("Input: " + input);
        if (input.length() < 3) {
            return Collections.emptyList();
        }
        var result = new ArrayList<Object>();
        var totalLength = input.length();

        for (int i = 0; i < totalLength; i++) {
            if (input.charAt(i) == 'l') {
                // System.out.println("i: " + i);
                var closingIndex = findListClosingIndex(input, i);
                // System.out.println("e: " + closingIndex);
                // System.out.println("Encountered 'l', skipping iteration");
                // var closingIndex = totalLength-1;
                // var ch = input.charAt(closingIndex);

                // while (true && ch != 'e') {
                    // closingIndex--;
                // }
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

    private static int findListClosingIndex(String input, int startIndex) {
        var ch = input.charAt(startIndex);
        var closingIndex = startIndex;
        var stack = new Stack<Character>();
        while (true) {
            if (ch == 'l' || ch == 'i') {
                stack.push(ch);
            } else if (ch == 'e') {
                stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
            }
            closingIndex++;
            ch = input.charAt(closingIndex);
        }
        return closingIndex;
    }
}
