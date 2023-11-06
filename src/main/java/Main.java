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
        final var decodeValues = BencodeDecoder.decode(bencodedValue);
        if (decodeValues.size() == 1) {
          System.out.println(decodeValues.get(0));
          return;
        }
        System.out.println(decodeValues);
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
