package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.CurrTickEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link Tick Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private int speed;
	private int duration;
	private Timer timer;
	private int tick;

	public TimeService(String name, int speed, int duration) {
		super(name);
		this.speed = speed;
		this.duration = duration;
		this.tick = 0;
		this.timer = new Timer();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, terminateBroadcast->{
			this.terminate();
		});

		subscribeEvent(CurrTickEvent.class, currTickEvent->{
			complete(currTickEvent, tick);
		});

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				tick++;
				if (tick == duration){
					sendBroadcast(new TerminateBroadcast());
					timer.cancel();
				}
				else{
					sendBroadcast(new TickBroadcast(tick));
				}
			}
		}, 0, speed);

//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate();
		
	}

}
