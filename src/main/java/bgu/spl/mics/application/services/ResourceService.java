package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AcquireVehicleEvent;
import bgu.spl.mics.application.messages.ReleseVehicleEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourceHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService{
	private ResourcesHolder resourcesHolder;

	public ResourceService(String name) {
		super(name);
		resourcesHolder = ResourcesHolder.getInstance();
	}

	@Override
	protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast-> {
            if (tickBroadcast.getToBeTerminated())
                terminate();
        });


        subscribeEvent(AcquireVehicleEvent.class, acquireVehicleEvent->{
            Future<DeliveryVehicle> deliveryVehicleFuture = resourcesHolder.acquireVehicle();
            complete(acquireVehicleEvent, deliveryVehicleFuture.get());
        });

        subscribeEvent(ReleseVehicleEvent.class, releseVehicleEvent->{
            resourcesHolder.releaseVehicle(releseVehicleEvent.getDeliveryVehicle());
        });
		
	}

}
