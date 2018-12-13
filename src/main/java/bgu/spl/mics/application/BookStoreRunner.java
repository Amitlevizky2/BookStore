package bgu.spl.mics.application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    private static final String filePath = "input.json";
    public static void main(String[] args) {
        String str_obj = new Gson().toJson(filePath);
        JsonObject json = new JsonParser().parse(str_obj).getAsJsonObject();
    }
}
