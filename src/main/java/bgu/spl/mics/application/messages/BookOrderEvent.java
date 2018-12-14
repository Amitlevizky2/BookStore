package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent implements Event<OrderReceipt> {
    private String bookTitle;
    private int timeTick;
    private Customer customer;

    public BookOrderEvent(String bookTitle, int timeTick, Customer customer) {
        this.bookTitle = bookTitle;
        this.timeTick = timeTick;
        this.customer = customer;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getTimeTick() {
        return timeTick;
    }

    public Customer getCustomer() {
        return customer;
    }
}
