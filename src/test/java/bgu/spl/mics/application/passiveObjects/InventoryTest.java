package bgu.spl.mics.application.passiveObjects;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory inventory;
    @Before
    public void setUp() throws Exception {
        inventory = Inventory.getInstance();
    }

    @Test
    public void loadOneItem() {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[3];
        inventoryInfo[0] = new BookInventoryInfo("Harry Potter", 1, 100);
        inventory.load(inventoryInfo);
        assertEquals(OrderResult.SUCCESFULLY_TAKEN, inventory.take("Harry Potter"));
    }

    @Test
    public void loadTwoItems()
    {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        inventory.load(inventoryInfo);
        inventory.take("The secret");
        inventory.take("The secret");
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("The secret"));
    }

    @Test
    public void NotExistLoad()
    {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        inventory.load(inventoryInfo);
        inventory.take("The secret");
        inventory.take("The secret");
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("Harry Potter"));
    }

    @Test
    public void takeReduceByOne() {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        inventory.load(inventoryInfo);
        inventory.take("The secret");
        assertEquals(1, inventoryInfo[0].getAmountInInventory());
    }

    @Test
    public void take()
    {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        assertEquals(OrderResult.NOT_IN_STOCK, inventory.take("Harry Potter"));
    }

    @Test
    public void checkAvailabiltyAndGetPriceIfExists() {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        inventory.load(inventoryInfo);
        assertEquals(100, inventory.checkAvailabiltyAndGetPrice("The secrete"));
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[1];
        inventoryInfo[0] = new BookInventoryInfo("The secret", 2, 100);
        inventory.load(inventoryInfo);
        assertEquals(100, inventory.checkAvailabiltyAndGetPrice("Harry Potter"));
    }

    @Test
    public void printInventoryToFile() {
        BookInventoryInfo[] inventoryInfo = new BookInventoryInfo[3];
        inventoryInfo[0] = new BookInventoryInfo("Harry Potter", 1, 100);
        inventoryInfo[1] = new BookInventoryInfo("The secret", 4, 300);
        inventoryInfo[2] = new BookInventoryInfo("The dddddd", 2, 500);
        inventory.load(inventoryInfo);
        inventory.take("Harry Potter");
        inventory.take("Harry Potter");
        inventory.printInventoryToFile("checkFile.txt");



        ConcurrentHashMap<Integer, String> map = null;
        try
        {
            FileInputStream fis = new FileInputStream("checkFile.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (ConcurrentHashMap<Integer, String>) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
        System.out.println("Deserialized HashMap..");
        // Display content using Iterator
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key: "+ mentry.getKey() + " & Value: ");
            System.out.println(mentry.getValue());
        }
    }
}