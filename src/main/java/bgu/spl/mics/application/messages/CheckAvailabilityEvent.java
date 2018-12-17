package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class CheckAvailabilityEvent implements Event<Integer> {
    private String bookTitle;
    private int availableAmount;
    public CheckAvailabilityEvent(String bookTitle, int availableAmount) {
        this.bookTitle = bookTitle;
        this.availableAmount = availableAmount;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }
}
