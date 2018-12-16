package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{
	private ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> bookOrderEvents;
	private Customer customer;
	private int tick;

	public APIService(Customer customer, ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> bookOrderEvents) {
		super("APIService" + customer.getId());
		this.customer = customer;
		this.bookOrderEvents = bookOrderEvents;
	}

	@Override
	protected void initialize() {
//		subscribeBroadcast(TickBroadcast.class, tickBroadCast->{
//			this.tick = tickBroadCast.getTick();
//		});

			subscribeBroadcast(TickBroadcast.class, tickBroadcast->{
				if (tickBroadcast.getToBeTerminated())
					terminate();
			int tick = tickBroadcast.getTick();
			LinkedBlockingQueue<String> ordersOfTick = bookOrderEvents.get(tick);
			if(ordersOfTick == null)
				return;
			while(!ordersOfTick.isEmpty())
			{
                String bookToOrder="";
				try {
					bookToOrder = ordersOfTick.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (bookToOrder.length() != 0){
				    BookOrderEvent bookOrderEvent = new BookOrderEvent(bookToOrder, tick, this.customer);
					Future<OrderReceipt> orderReceiptFuture = sendEvent(bookOrderEvent);
				    OrderReceipt orderReceipt = orderReceiptFuture.get();
                    customer.addReciptToList(orderReceipt);
				}
			}
		});



		
	}

}
