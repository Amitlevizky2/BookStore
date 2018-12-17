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
	int tick;
	public SellingService(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, tickBroadcast-> {
            tick = tickBroadcast.getTick();
            if (tickBroadcast.getToBeTerminated())
                terminate();
        });


        subscribeEvent(BookOrderEvent.class, bookOrderEvent->{
            Customer c = bookOrderEvent.getCustomer();
            Future<Integer> checkAvailabilityFuture = sendEvent(new CheckAvailabilityEvent(bookOrderEvent.getBookTitle(), c.getAvailableCreditAmount()));
            if(checkAvailabilityFuture.get() != null && checkAvailabilityFuture.get() != -1) {
                if (c.getAvailableCreditAmount() < checkAvailabilityFuture.get()) {
                    complete(bookOrderEvent, null);
                    return;
                }
                //Future<Integer> bookPriceFurture = sendEvent(new C(bookOrderEvent.getBookTitle()));
                //////////////////////////////System.out.println(takeBookFuture.get())
                    moneyRegister.chargeCreditCard(c, checkAvailabilityFuture.get());
                    OrderReceipt orderReceipt = new OrderReceipt(0, this.getName(), c.getId(), bookOrderEvent.getBookTitle(),
                            checkAvailabilityFuture.get(), this.tick,bookOrderEvent.getTimeTick(), this.tick);
//                    System.out.println("orderIs= " + orderReceipt.getOrderId() + " customername: "+ orderReceipt.getSeller()
//                            + " bookTitle: "+ orderReceipt.getBookTitle() + " price: "+ orderReceipt.getPrice() + " issudTick: "+ orderReceipt.getIssuedTick()
//                    + " orcerTick: "+ orderReceipt.getOrderTick() + " proccessTick: "+ orderReceipt.getProcessTick());
                    moneyRegister.file(orderReceipt);
                    complete(bookOrderEvent, orderReceipt);
                    sendEvent(new DeliveryEvent(c.getDistance(), c.getAddress()));
                    return;
                }

            complete(bookOrderEvent, null);

        });
		
	}

}
