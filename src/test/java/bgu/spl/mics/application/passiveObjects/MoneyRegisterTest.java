package bgu.spl.mics.application.passiveObjects;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class MoneyRegisterTest {

    MoneyRegister money;
    OrderReceipt neword1;
    OrderReceipt neword2;
    OrderReceipt neword3;

    @Before
    public void setUp() throws Exception {
         neword1 = new OrderReceipt(10,"amit", 12, "Secret", 100, 1, 2, 3);
         neword2 = new OrderReceipt(12,"amit", 12, "Secret", 100, 1, 2, 3);
         neword3 = new OrderReceipt(14,"amit", 12, "Secret", 100, 1, 2, 3);
         money = MoneyRegister.getInstance();
    }

    @Test
    public void getInstance() {
    }

    @Test
    public void file() {

    }

    @Test
    public void getTotalEarnings() {
    }

    @Test
    public void chargeCreditCard() {
    }

    @Test
    public void printOrderReceipts() {
        money.file(neword1);
        money.file(neword2);
        money.file(neword3);
        money.printOrderReceipts("OrderRecipts.txt");
        List<OrderReceipt> orders = new ArrayList<>();
        try
        {
            FileInputStream fis = new FileInputStream("OrderRecipts.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            orders = (ArrayList<OrderReceipt>) ois.readObject();
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

        for (OrderReceipt order:orders) {
            System.out.println(order.getCustomerId() + " " + order.getBookTitle());
        }
    }

    @Test
    public void getOrderReceipts() {
    }
}