package bgu.spl.mics.application.passiveObjects;

import org.junit.Before;
import org.junit.Test;

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

    }
}