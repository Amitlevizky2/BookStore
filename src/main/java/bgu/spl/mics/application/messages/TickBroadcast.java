package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int tick;
    private Boolean isToBeTerminated;

    public TickBroadcast(int tick, Boolean isToBeTerminated) {
        this.tick = tick;
        this.isToBeTerminated = isToBeTerminated;
    }

    public Boolean getToBeTerminated() { return isToBeTerminated;}

    public int getTick() {
        return tick;
    }
}
