package bgu.spl.mics.application;

import bgu.spl.mics.LevizkyEvganyParser;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    //private static final String filePath = "input.json";
    public static void main(String[] args) {
//        String str_obj = new Gson().toJson(filePath);
//        JsonObject json = new JsonParser().parse(str_obj).getAsJsonObject();
        Gson gson = new Gson();
        LevizkyEvganyParser dataFromJson = null;
        try {
            dataFromJson=gson.fromJson(new FileReader("/Users/avivlevitzky/Downloads/SPL_PROJECT_2-/src/main/java/bgu/spl/mics/application/sample.json"), LevizkyEvganyParser.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<LevizkyEvganyParser.InitialInventory> initialInventorie = dataFromJson.getInitialInventorie();
        BookInventoryInfo[] books = new BookInventoryInfo[dataFromJson.getInitialInventorie().size()];
        for (LevizkyEvganyParser.InitialInventory initiateInventory: initialInventorie) {

        }

//        loadInitialInvemtory(dataFromJson.getInitialInventorie());
//        loadInitialResources(dataFromJson.getInitialResources());
//        initiateServices();
//        initiateCustomers(dataFromJson.getCustomers());
    }
}
