package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CheckAvailabilityEvent;
import bgu.spl.mics.application.messages.CurrTickEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{
	MoneyRegister moneyRegister = MoneyRegister.getInstance();

	public SellingService(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast->{
            terminate();
        });

        subscribeEvent(BookOrderEvent.class, bookOrderEvent->{
            Customer c = bookOrderEvent.getCustomer();
            Future<Integer> proccessTickFuture = sendEvent(new CurrTickEvent());
            Future<Integer> checkAvailabilityFuture = sendEvent(new CheckAvailabilityEvent(bookOrderEvent.getBookTitle()));
            if(checkAvailabilityFuture.get() != -1)
                synchronized (c){
                    if (c.getAvailableCreditAmount() < checkAvailabilityFuture.get()) {
                        complete(bookOrderEvent, null);
                        notify();
                        return;
                    }
                    moneyRegister.chargeCreditCard(c, checkAvailabilityFuture.get());

                }
        });
		
	}

}
