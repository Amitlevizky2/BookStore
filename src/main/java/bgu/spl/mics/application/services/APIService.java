package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.CurrTickEvent;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.List;

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
	private List<BookOrderEvent> bookOrderEvents;

	public APIService(String name, List<BookOrderEvent> bookOrderEvents) {
		super(name);
		this.bookOrderEvents = bookOrderEvents;
	}

	@Override
	protected void initialize() {
//		subscribeBroadcast(TickBroadcast.class, tickBroadcast->{
//			sendEvent()
//		});
		
	}

}
