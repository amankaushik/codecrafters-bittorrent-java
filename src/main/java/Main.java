// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {

  public static void main(String[] args) throws Exception {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    // System.out.println("Logs from your program will appear here!");
    String command = args[0];
    if("decode".equals(command)) {
      //  Uncomment this block to pass the first stage
       String bencodedValue = args[1];
      //  String decoded;
       try {
        final var decodeValues = BencodeDecoder._decode(bencodedValue);
        if (decodeValues.result.size() == 1) {
          System.out.println(decodeValues.result.get(0));
          return;
        }
        System.out.println(decodeValues.result);
       } catch(RuntimeException e) {
         System.out.println(e.getMessage());
         return;
       }
    } else {
      System.out.println("Unknown command: " + command);
    }
  }

  static String decodeBencodeInteger(String bencoString) {
      return bencoString.substring(1, bencoString.length() - 1);
  }
}
