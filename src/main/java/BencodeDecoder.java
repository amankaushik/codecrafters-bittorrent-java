import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
// import java.util.TreeMap;
import java.util.List;
import java.util.Map;

// import com.google.gson.Gson;

public class BencodeDecoder {
    static List<Object> result = new ArrayList<>();
    // private static final Gson gson = new Gson();

    static DecoderResult _decode(String input) {
        // System.out.println("Input: " + input);

        if (input.length() < 3) {
            if (input.startsWith("d")) {
                return new DecoderResult(Collections.singletonList("{}"), 0);
            }
            return new DecoderResult(Collections.emptyList(), 0);
        }

        var result = new ArrayList<Object>();
        var totalLength = input.length();
        int i = 0;

        for (; i < totalLength; i++) {
            if (input.charAt(i) == 'd') {
                var op = _decode(input.substring(i + 1));
                // System.out.println(op.result);
                Map<Object, Object> resMap = new HashMap();
                for (int k = 0; k < op.result.size(); k += 2) {
                    resMap.put(op.result.get(k), op.result.get(k + 1));
                }
                // System.out.print(gson.toJson(resMap).replaceAll("\\\\", "").replace("\"\"", "\""));
                // System.out.println(resMap.toString().replaceAll("=", ":").replaceAll(" ", ""));
                result.add(resMap.toString().replaceAll("=", ":").replaceAll(" ", ""));
                i = op.nextIndex;
            }
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
}
