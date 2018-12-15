package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer {

	private int id;
	private String name;
	private String address;
	private int distance;
	private List<OrderReceipt> Receipts;
	private int creditCard;
	private AtomicInteger availableCreditAmount;

	public Customer(int id, String name, String address, int distance, int creditCard, int availableCreditAmount) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.distance = distance;
		this.creditCard = creditCard;
		this.availableCreditAmount = new AtomicInteger(availableCreditAmount);
		Receipts = new ArrayList<>();
	}

	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return this.name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return this.id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return this.address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return this.distance;
	}

	
	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return this.Receipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public int getAvailableCreditAmount() {
			return this.availableCreditAmount.get();
		}
	
	/**
     * Retrieves this customers credit card serial number.    
     */
	public int getCreditNumber() {
		return this.creditCard;
	}

	public void chargeCustomer(int amount){
		int localVal;
		do{
			localVal = this.availableCreditAmount.get();
		}while(!availableCreditAmount.compareAndSet(localVal, localVal-amount));//TODO how does it solve my problam??
	}

	public void addReciptToList(OrderReceipt orderReceipt)
	{
	    if(orderReceipt != null)
		    Receipts.add(orderReceipt);
	}
	
}
