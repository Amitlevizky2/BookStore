# BookStore

Multi-Threaded Micro-Service framework, which used to implementing 
an online book store with a delivery option.
This assignment is composed of two main sections:
1.  Simple Micro-Service framework.
2.  An online books store application on top of this framework.

### PART 1: SYNCHRONUS MICROSERVICES FRAMEWORK
Micro-Service framework that consists of two main parts: A Message-Bus
and Micro-Services. Each Micro-Service is a thread that can exchange 
messages with other Micro-Services using a shared object referred to as the Message-Bus.
There are two different types of messages:
* **Event** defines an action that needs to be processed, e.g., ordering a book from a store. Each
Micro-Service specializes in processing one or more types of events. Upon receiving an event,
the Message-Bus assigns it to the messages queue of an appropriate Micro-Service which
specializes in handling this type of event. It is possible that there are several Micro-Services
which specialize in the same events, (e.g., two or more Micro-Services which can handle an
event of ordering a book from a books store). In this case, the Message-Bus assigns the event
to a single Micro-Service of those which specialize in this event in a round-robin manner.
* **Broadcast** messages represents a global announcement in the system. Each Micro-Service can
subscribe to the type of broadcast messages it is interested to receive. The Message-Bus sends the
broadcast messages that are passed to it to all the Micro-Services which are interested in receiving them
(this is in contrast to events which are sent to only one of the Micro-Services which are interested in them).

**Message loop pattern**
Each micro-service is a thread which runs a loop. In each iteration of the loop, the thread tries to 
fetch a message from its queue and process it.

#### Components
* **Broadcast**: A Marker interface extending Message. When sending Broadcast messages using the
Message-Bus it will be received by all the subscribers of this Broadcast-message type (the message
Class).
* **Event<T>**: A marker interface extending Message. A Micro-Service that sends an Event message
expects to be notified when the Micro-Service that processes the event has completed processing
it. The event has a generic type variable T, which indicates its expected result type (should be
passed back to the sending Micro-Service). The Micro-Service that has received the event must
call the method ‘Complete’ of the Message-Bus once it has completed treating the event, in order
to resolve the result of the event.
* **MessageBus**: The Message-Bus is a shared object used for communication between MicroServices.
It should be implemented as a thread-safe singleton, as it is shared between all the MicroServices in the system.
The implementation of the MessageBus interface should be inside the class
MessageBusImpl (provided to you). There are several ways in which you can implement the
message-bus methods; be creative and find a good, correct and efficient solution.
* **MicroService**: The MicroService is an abstract class that any Micro-Service in the system must
extend. The abstract MicroService class is responsible to get and manipulate the singleton
MessageBus instance.
*  **Future**: Future object represents a promised result - an object that will eventually be 
resolved to hold a result of some operation.



### PART 2: BUILDING AN ONLINE BOOK STORE
 An online book store management system that was built on top the framework from part 1.
**The program flow**:
A customer connects to the store website and orders a book. If the book is available and the customer has
enough credit, the order is confirmed - the customer pays for the book, and then the book will be delivered 
to his address as soon as possible.

#### Components
* **BookInventoryInfo**: An object which represents information about a single book in the storeץ
* **OrderReceipt**: An object representing a receipt that should be sent to a customer after buying a
book (when the customers OrderBookEvent has been completed).
* **Inventory**:This object is a thread safe singleton. The Inventory object holds a collection of BookInventoryInfo:
One for each book the store offers.
* **DeliveryVehicle**: This object represents a delivery vehicle in the system.
* **MoneyRegister**: This object holds a list of receipt issued by the store. This class is a thread safe singleton.
* **ResourcesHolder**: Holds a collection of DeliveryVehicle.
* **Customer**: This object represents a Buyer in the online book store.

The following components are concrete components of the framework implemented in the first part of the program:
* **BookOrderEvent**: An event that is sent when a client of the store wishes to buy a book. Its expected response
type is an OrderReceipt.
* **TickBroadcast**: A broadcast messages that is sent at every passed clock tick.
* **DeliveryEvent**: An event that is sent when the BookOrderEvent is successfully completed and a delivery is
required.
* **TimeService**: This Micro-Service is our global system timer (handles the clock ticks in the system). It is
responsible for counting how much clock ticks passed since its initial execution and notify every
other Micro-Service (that is interested) about it using the TickBroadcast.
* **APIService**: This Micro-Service describes one client connected to the application.
* **SellingService**: This Micro-Service handles OrderBookEvent.
* **LogisticsService**:  This Micro-Service handles the DeliveryEvent.

# Built With
- OOP
- [Micro-Services](https://microservices.io/)
- [Multithreading](https://en.wikipedia.org/wiki/Multithreading_(computer_architecture))

### Tech
* [Java](https://www.java.com/)
* [IntelliJ](https://www.jetbrains.com/idea/)
* [Github](https://github.com/)

### Authors
* Amit Levizky
* Evgeny Kaidrikov
