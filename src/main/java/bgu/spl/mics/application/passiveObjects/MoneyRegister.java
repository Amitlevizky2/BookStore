package bgu.spl.mics.application.passiveObjects;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;


/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
    private int totalEarnings;
    private List<OrderReceipt> orderReceipts;
    private int orderId;

	private static class SingeltonHolder{
        private static MoneyRegister instance = new MoneyRegister();
    }

    private MoneyRegister() {
        totalEarnings = 0;
        orderId = 1;
        orderReceipts = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Retrieves the single instance of this class.
     */
	public static MoneyRegister getInstance() {
	    return SingeltonHolder.instance;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		if (r != null) {
			r.setOrderId(orderId);
			orderReceipts.add(r);
			orderId++;
		}
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return this.totalEarnings;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	public void chargeCreditCard(Customer c, int amount) {
		c.chargeCustomer(amount);
		totalEarnings+= amount;
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.. 
     */
	public void printOrderReceipts(String filename) {
		if (orderReceipts == null)
			return;
		try{
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(orderReceipts);
			oos.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Could not write hashmap to the file");
		}
	}

    public List<OrderReceipt> getOrderReceipts() {
        return this.orderReceipts;
    }
}
