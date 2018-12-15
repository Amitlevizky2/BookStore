package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

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
            Future<Integer> issuedTick;
            Future<Integer> checkAvailabilityFuture = sendEvent(new CheckAvailabilityEvent(bookOrderEvent.getBookTitle()));
            if(checkAvailabilityFuture.get() != -1) {
                if (c.getAvailableCreditAmount() < checkAvailabilityFuture.get()) {
                    complete(bookOrderEvent, null);
                    return;
                }
                Future<Boolean> takeBookFuture = sendEvent(new TakeBookEvent(bookOrderEvent.getBookTitle()));
                if (takeBookFuture.get()) {
                    moneyRegister.chargeCreditCard(c, checkAvailabilityFuture.get());
                    issuedTick = sendEvent(new CurrTickEvent());
                    OrderReceipt orderReceipt = new OrderReceipt(0, this.getName(), c.getId(), bookOrderEvent.getBookTitle(),
                            checkAvailabilityFuture.get(), bookOrderEvent.getTimeTick(), proccessTickFuture.get(), issuedTick.get());
                    complete(bookOrderEvent, orderReceipt);
                    moneyRegister.file(orderReceipt);
                    sendEvent(new DeliveryEvent(c.getDistance(), c.getAddress()));
                    return;
                }
            }
            complete(bookOrderEvent, null);

        });
		
	}

}
