package bgu.spl.mics.application;

import bgu.spl.mics.LevizkyEvganyParser;
import bgu.spl.mics.LevizkyEvganyParser.*;
import bgu.spl.mics.LevizkyEvganyParser.InitialResources.Vehicle;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    static List<Runnable> services = new ArrayList<>();
    public static void main(String[] args) {
        HashMap<Integer, Customer> customerHashMap;
        List<Thread> threads = new LinkedList<>();
        TimeService timeService;
        Gson gson = new Gson();
        LevizkyEvganyParser dataFromJson = null;
        try {
            dataFromJson=gson.fromJson(new FileReader(args[0]), LevizkyEvganyParser.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadInitialInvemtory(dataFromJson.getInitialInventory());
        loadInitialResources(dataFromJson.getInitialResources().get(0).getVehicles());
        customerHashMap = initiateCustomerAPIService(dataFromJson);
        timeService = initiateTimeServic(dataFromJson.getServices().getTime().getSpeed(), dataFromJson.getServices().getTime().getDuration());
        initiateServices(dataFromJson);
        for (Runnable service:services) {
            Thread t = new Thread(service);
            t.start();
            threads.add(t);
        }
        Thread time = new Thread(timeService);
        time.start();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            time.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printToFile(args[1], customerHashMap);
//        customersFile(args[0], customerHashMap);
        Inventory.getInstance().printInventoryToFile(args[2]);
        MoneyRegister.getInstance().printOrderReceipts(args[3]);
        printToFile(args[4], MoneyRegister.getInstance());
//        MoneyRegisterFile(args[3], MoneyRegister.getInstance());
    }

    private static void printToFile(String arg, Object o) {
        if (o == null)
            return;
        try{
            FileOutputStream fos = new FileOutputStream(arg);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Could not write hashmap to the file");
        }
    }


    private static void initiateServices(LevizkyEvganyParser dataFromJson) {
        initiateSellingServices(dataFromJson.getServices().getSelling());
        intiateInventory(dataFromJson.getServices().getInventoryService());
        initiateLogistics(dataFromJson.getServices().getLogistics());
        initiateResorces(dataFromJson.getServices().getResourcesService());

    }

    private static void initiateResorces(int resourcesService) {
        for (int i = 0; i < resourcesService; i++) {
            ResourceService resourceService = new ResourceService("resourcesService "+ (i+1));
            services.add(resourceService);
        }
    }

    private static void initiateLogistics(int logistics) {
        for (int i = 0; i < logistics; i++) {
            LogisticsService logisticsService =  new LogisticsService("logistics " + (i+1));
            services.add(logisticsService);

        }
    }

    private static void intiateInventory(int inventoryService) {
        for (int i = 0; i <inventoryService ; i++) {
            InventoryService inventoryService1 = new InventoryService("inventoryService " + (i+1));
            services.add(inventoryService1);
        }
    }

    private static HashMap<Integer, Customer> initiateCustomerAPIService(LevizkyEvganyParser dataFromObject) {
        Services.Cust[] customers = dataFromObject.getServices().getCustomers();
        HashMap<Integer, Customer> cust = new HashMap<>();
        ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> bookOrderEvents;
        for (Services.Cust customer : customers) {
            Customer c = new Customer(customer.getId(), customer.getName(), customer.getAddress(),
                    customer.getDistance(), customer.getCreditCard().getNumber(), customer.getCreditCard().getAmount());
            cust.putIfAbsent(c.getId(), c);
            bookOrderEvents = listToConcurrentHashMap(customer.getOrderSchedule());
            APIService api = new APIService(c, bookOrderEvents);
            services.add(api);
        }
        return cust;

    }

    private static ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> listToConcurrentHashMap(List<Services.Cust.OrderSchedule> orderSchedule) {
        ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> hashmap = new ConcurrentHashMap<>();
        LinkedBlockingQueue<String> queue;
        for (Services.Cust.OrderSchedule orders : orderSchedule) {
            queue = hashmap.get(orders.getTick());
            if(queue == null) {
                hashmap.put(orders.getTick(),queue = new LinkedBlockingQueue<>());
                queue.add(orders.getBookTitle());
            }
            else
            {
                queue.add(orders.getBookTitle());
            }
        }
        return hashmap;
    }

    private static void initiateSellingServices(int selling)
    {
        for (int i = 0; i < selling; i++) {
            SellingService sellingService = new SellingService("selling " + (i+1) );
            services.add(sellingService);
        }
    }
    private static TimeService initiateTimeServic(int speed, int duration) {
        TimeService timeService = new TimeService("TimeService",speed, duration);
        return timeService;
    }

    private static void loadInitialResources(Vector<Vehicle> initialResourcesToLoad) {
        DeliveryVehicle[] deliveryVehicles = new DeliveryVehicle[initialResourcesToLoad.size()];
        for (int i = 0; i < initialResourcesToLoad.size(); i++) {
            Vehicle v = initialResourcesToLoad.get(i);
            deliveryVehicles[i] = new DeliveryVehicle(v.getLicense(), v.getSpeed());
        }
        ResourcesHolder.getInstance().load(deliveryVehicles);
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
