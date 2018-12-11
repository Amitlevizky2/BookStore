package bgu.spl.mics.application.passiveObjects;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private Vector <BookInventoryInfo> booksInventoryInfo;

	private static class InventoryHolder{
		private static Inventory instance = new Inventory();
	}

	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return InventoryHolder.instance;
	}
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		booksInventoryInfo = new Vector<>();
		this.booksInventoryInfo.addAll(Arrays.asList(inventory));
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
			BookInventoryInfo temp = findBookByTitle(book);
			if (temp == null)
				return OrderResult.NOT_IN_STOCK;
			temp.reduceAmountInInventory();
			return OrderResult.SUCCESFULLY_TAKEN;
	}
	
	
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		BookInventoryInfo temp = findBookByTitle(book);
		if(temp == null)
			return -1;
		return temp.getPrice();
	}

	/**
	 * By given a book title, will return the book if this book exists in the inventory, else, return null
	 * @param book
	 * @return temp
	 */
	private BookInventoryInfo findBookByTitle(String book){
		for (BookInventoryInfo temp : booksInventoryInfo) {
			if (book.equals(temp.getBookTitle())) {
				return temp;
			}
		}
		return null;
	}

	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		HashMap<String,Integer> inventoryHashMap = collectionToHashMap();
		if (inventoryHashMap == null)
			return;
		try{
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(inventoryHashMap);
			oos.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Could not write hashmap to the file");
		}
	}

	/**
	 * Creates the HashMap, from the BookInventoryCollection, that will be written to a file later
	 * @return	HashMap representing the info (Book Title and Amount) of every book in Inventory
	 */
	private HashMap<String, Integer> collectionToHashMap(){
		if(booksInventoryInfo.size() == 0)
			return null;
		HashMap<String, Integer> hmap = new HashMap<>();
		for (BookInventoryInfo temp: booksInventoryInfo) {
			hmap.put(temp.getBookTitle(), temp.getAmountInInventory());
		}
		return hmap;
	}


}
