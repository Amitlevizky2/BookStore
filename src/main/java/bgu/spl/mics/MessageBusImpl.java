package bgu.spl.mics;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private Map<Class, LinkedBlockingQueue<MicroService>> roundRobinMap;
	private Map<MicroService, LinkedBlockingQueue<Message>> microServiceQueueMap;
	private Map<Message, Future> eventFutureMap;
	private Map<MicroService, LinkedBlockingQueue<Future>> microServiceFutureMap;

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
    	Queue tempQueue = roundRobinMap.get(type);
    	if(tempQueue == null) {
            synchronized (tempQueue) {//TODO: Check if needed
                if (!tempQueue.contains(type))
                    tempQueue.add(type);
            }
        }
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		Queue tempQueue = roundRobinMap.get(m);
		if (tempQueue != null)
		synchronized (tempQueue) {//TODO: Check if needed
			if(!tempQueue.contains(type))
				tempQueue.add(type);
		}

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);//TODO: need Sync??
		result.notifyAll();
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		Queue<MicroService> tempMicroServiceQueue = roundRobinMap.get(b.getClass());
		if(tempMicroServiceQueue!=null) {
            synchronized (tempMicroServiceQueue) {
                for (MicroService tempMicroService : tempMicroServiceQueue) {
                    Queue<Message> tempMessagesQueue = microServiceQueueMap.get(tempMicroService);
                    if (tempMessagesQueue == null) {
                        synchronized (tempMessagesQueue) {
                            tempMessagesQueue.add(b);
                        }
                    }
                }
            }
        }
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
    	MicroService m = null;
		LinkedBlockingQueue<MicroService> tempMicroServiceQueue = roundRobinMap.get(e.getClass());
		if(tempMicroServiceQueue == null)
			return null;
		synchronized (tempMicroServiceQueue){
			try {
				m = tempMicroServiceQueue.take();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			tempMicroServiceQueue.add(m);
			microServiceQueueMap.get(m).add(e);
		}

		Future<T> f1 = new Future<>();
		eventFutureMap.put(e, f1);
		LinkedBlockingQueue<Future> tempFutureQueue =  microServiceFutureMap.get(m);
		tempFutureQueue.add(f1);
		return (new Future<>());
	}

	@Override
	public void register(MicroService m) {
    	microServiceQueueMap.put(m, new LinkedBlockingQueue<>());
    	microServiceFutureMap.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
    	synchronized (m){
			LinkedBlockingQueue<Future> tempFutureQueue = microServiceFutureMap.get(m);
			for (Future f1 : tempFutureQueue){
				f1.resolve(null);
			}

		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
        Message msgToSend = null;
        try{
            LinkedBlockingQueue<Message> tempMessageQueue = microServiceQueueMap.get(m);
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
