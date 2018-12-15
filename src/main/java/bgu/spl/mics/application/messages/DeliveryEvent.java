package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

public class DeliveryEvent implements Event <DeliveryVehicle> {

    private final int distance;
    private final String address;

    public DeliveryEvent(int distance, String address) {
        this.distance = distance;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public int getDistance() {
        return distance;
    }
}
