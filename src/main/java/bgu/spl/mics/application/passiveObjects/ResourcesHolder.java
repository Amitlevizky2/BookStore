package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {
	private Queue<DeliveryVehicle> deliveryVehicles;
    private Semaphore semaphore;
	private static class singeltonHolder{
		private static ResourcesHolder instance = new ResourcesHolder();
	}

    public ResourcesHolder() {
		this.deliveryVehicles = new LinkedList<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
	public static ResourcesHolder getInstance() {
		return singeltonHolder.instance;
	}
	
	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		Future<DeliveryVehicle> tempVehicleFuture = new Future<>();
		tempVehicleFuture.resolve(deliveryVehicles.poll());
		return tempVehicleFuture;
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		deliveryVehicles.add(vehicle);
		semaphore.release();
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		if (vehicles.length > 0) {
			this.deliveryVehicles.addAll(Arrays.asList(vehicles));
			semaphore = new Semaphore(deliveryVehicles.size(), true);
		}
	}

}
