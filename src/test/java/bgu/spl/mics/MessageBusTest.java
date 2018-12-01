package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleBroadcastListenerService;
import bgu.spl.mics.example.services.ExampleEventHandlerService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageBusTest {

    private MessageBus msgBus;
    private MicroService eventHandlerService;
    private MicroService broadcastHandlerService;
    private Broadcast broadcast;
    private Event event;
    private Future future;

    @Before
    public void setUp() throws Exception
    {
        msgBus = MessageBusImpl.getInstance();
        event = new ExampleEvent("ExampleMessageSenderService");
        //messageSenderService = new ExampleMessageSenderService("amit", new String[] {"3"});
        broadcast = new ExampleBroadcast("amit");
        eventHandlerService = new ExampleEventHandlerService("exampleEventHandlerService", new String[]{"3"});
        broadcastHandlerService = new ExampleBroadcastListenerService("exampleBroadcastHandlerService", new String[]{"3"});
        eventHandlerService.initialize();
        broadcastHandlerService.initialize();
    }

    @Test
    public void subscribeEvent() {
        msgBus.register(eventHandlerService);
        msgBus.subscribeEvent(ExampleEvent.class, eventHandlerService);
        future = msgBus.sendEvent(event);
        assertNotNull(future);
    }

    @Test
    public void subscribeBroadcast() {
        msgBus.register(broadcastHandlerService);
        msgBus.subscribeBroadcast(ExampleBroadcast.class, broadcastHandlerService);
        msgBus.sendBroadcast(broadcast);

        try {
            Message broad = msgBus.awaitMessage(broadcastHandlerService);
            assertNotNull(broad);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void complete() {
        msgBus.register(eventHandlerService);
        msgBus.subscribeEvent(ExampleEvent.class, eventHandlerService);
        future = msgBus.sendEvent(event);
        msgBus.complete(event, future);
        assertTrue(future.isDone());
    }

    @Test
    public void sendBroadcast() {
        msgBus.register(broadcastHandlerService);
        msgBus.subscribeBroadcast(ExampleBroadcast.class, broadcastHandlerService);
        msgBus.sendBroadcast(broadcast);

        try {
            Message broad = msgBus.awaitMessage(broadcastHandlerService);
            assertNotNull(broad);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendEvent()
    {
        msgBus.register(eventHandlerService);
        msgBus.subscribeEvent(ExampleEvent.class, eventHandlerService);
        future = msgBus.sendEvent(event);
        assertNotNull(future);
    }

    @Test
    public void register() {

    }

    @Test
    public void unregister() {
    }

    @Test
    public void awaitMessage() {
    }
}