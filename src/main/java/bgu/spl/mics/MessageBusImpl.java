package bgu.spl.mics;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private Map<Class, LinkedBlockingDeque<MicroService>> roundRobinMap;
	private Map<MicroService, LinkedBlockingDeque<Message>> microServiceQueueMap;
	private Map<Message, Future> eventFutureMap;
	private Map<MicroService, LinkedBlockingDeque<Future>> microServiceFutureMap;
	private Object obj = new Object();
	private Object obj1 = new Object();
	private Object obj2 = new Object();
	private Object obj3 = new Object();

    private static class MessageBusSingelton{
        private static MessageBusImpl instance = new MessageBusImpl();
    }

	public MessageBusImpl() {
		this.roundRobinMap = new ConcurrentHashMap<>();
		this.microServiceQueueMap = new ConcurrentHashMap<>();
		this.eventFutureMap = new ConcurrentHashMap<>();
		this.microServiceFutureMap = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance()
    {
        return MessageBusSingelton.instance;
    }
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
    	subscribe(type, m);
     }

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
    	subscribe(type, m);
	}

	private void subscribe(Class type, MicroService m) {
		if (type == null || m == null)
			return;
		synchronized (obj) {
			roundRobinMap.putIfAbsent(type, new LinkedBlockingDeque<MicroService>());
            try {
                roundRobinMap.get(type).put(m);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
    	Future <T> future = eventFutureMap.get(e);
            future.resolve(result);//TODO: need Sync??
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		synchronized (obj1){
		if (b == null)
			return;
		Queue<MicroService> tempMicroServiceQueue = roundRobinMap.get(b.getClass());
		if (tempMicroServiceQueue == null)
			return;
		for (MicroService micro : tempMicroServiceQueue) {
			addMessageToMicroService(b, micro);
		}
	}

	}
	private void addMessageToMicroService(Message msg, MicroService m){
			LinkedBlockingDeque<Message> tempMicroQueue = microServiceQueueMap.get(m);
            if (tempMicroQueue == null)
                return;
            else {
				try {
					tempMicroQueue.put(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
    }

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
    	synchronized (obj1) {
    	//	synchronized (obj2) {
				MicroService m = null;
				synchronized (obj2){
				LinkedBlockingDeque<MicroService> tempMicroServiceQueue = roundRobinMap.get(e.getClass());
				//synchronized (tempMicroServiceQueue) {
					if (tempMicroServiceQueue == null || tempMicroServiceQueue.isEmpty())
						return null;

					try {
						m = tempMicroServiceQueue.take();
						//System.out.println(m.getName());
						//System.out.println(e.getClass() + "\n");
						tempMicroServiceQueue.put(m);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					Future<T> f1 = new Future<>();

					if (m != null) {
						eventFutureMap.putIfAbsent(e, f1);
						addMessageToMicroService(e, m);
					}
					LinkedBlockingDeque<Future> tempFutureQueue = microServiceFutureMap.get(m);
					//if(f1!= null && tempFutureQueue!= null)
					tempFutureQueue.add(f1);

					return f1;
				}
			}

	}

	@Override
	public void register(MicroService m) {
		if (microServiceQueueMap.get(m) == null) {
			microServiceQueueMap.putIfAbsent(m, new LinkedBlockingDeque<>());
			microServiceFutureMap.putIfAbsent(m, new LinkedBlockingDeque<>());
		}
	}

	@Override
	public void unregister(MicroService m) {

		LinkedBlockingDeque<Future> tempFutureQueue = microServiceFutureMap.get(m);
			for (Future f1 : tempFutureQueue){
			    if(!f1.isDone())
				    f1.resolve(null);
			    //tempFutureQueue.remove(f1);
			}
			microServiceFutureMap.remove(m);

            for (Class cls:roundRobinMap.keySet()) {
				LinkedBlockingDeque<MicroService> tempMicroServiceQueue = roundRobinMap.get(cls);
                tempMicroServiceQueue.remove(m);
            }

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
        Message msgToSend = null;
        try{
			LinkedBlockingDeque<Message> tempMessageQueue = microServiceQueueMap.get(m);
            if (tempMessageQueue == null)
                return null;
            msgToSend = tempMessageQueue.take();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return null;
        }
		return msgToSend;
	}

	

}
