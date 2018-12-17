package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
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
	private int tick;

	public TimeService(String name, int speed, int duration) {
		super(name);
		this.speed = speed;
		this.duration = duration;
		this.tick = 0;
	}

	@Override
	protected void initialize() {

		subscribeBroadcast(TickBroadcast.class, tickBroadcast->{
			if(tick == tickBroadcast.getTick())
				terminate();
		});
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				{
					//System.out.println(tick);
					if (duration <= tick){
						timer.cancel();
						timer.purge();
						sendBroadcast(new TickBroadcast(tick, true));
					}
					else{
						sendBroadcast(new TickBroadcast(tick, false));
						tick++;
					}
				}
			}
		};
		timer.schedule(timerTask, speed, speed);
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate();
		
	}

}
