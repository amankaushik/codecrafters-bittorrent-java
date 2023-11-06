import com.google.gson.Gson;

public class StringDecoder {
    private static final Gson gson = new Gson();

    public static String decode(String input) {
        return gson.toJson(input);
    }
}