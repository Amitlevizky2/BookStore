package bgu.spl.mics.application;

import bgu.spl.mics.LevizkyEvganyParser;
import bgu.spl.mics.LevizkyEvganyParser.*;
import bgu.spl.mics.LevizkyEvganyParser.InitialResources.Vehicle;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Vector;

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

        loadInitialInvemtory(dataFromJson.getInitialInventory());
        loadInitialResources(dataFromJson.getInitialResources().get(0).getVehicles());
//        initiateServices();
//        initiateCustomers(dataFromJson.getCustomers());
    }

    private static void loadInitialResources(Vector<Vehicle> initialResourcesToLoad) {
        LinkedList<Vehicle> vehicles = new LinkedList<>();
        for (Vehicle vehicle :initialResourcesToLoad) {
            vehicles.add(new Vehicle(vehicle.getLicense(), vehicle.getSpeed()));
        }
    }

    private static void loadInitialInvemtory(InitialInventory[] initialInventory) {
        if(initialInventory == null)
            return;
        BookInventoryInfo[] books = new BookInventoryInfo[initialInventory.length];
        for (int i = 0; i < books.length; i++) {
            books[i] = new BookInventoryInfo(initialInventory[i].getBookTitle(), initialInventory[i].getAmount(), initialInventory[i].getPrice());
        }
        Inventory.getInstance().load(books);
    }
}
