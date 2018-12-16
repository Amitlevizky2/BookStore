package bgu.spl.mics;

import java.util.List;
import java.util.Vector;

public class LevizkyEvganyParser {
    private InitialInventory[] initialInventory;
    private List<InitialResources> initialResources;
    private Services services;


    public class InitialInventory {
        private String bookTitle;
        private int amount;
        private int price;

        public String getBookTitle() {
            return bookTitle;
        }

        public int getAmount() {
            return amount;
        }

        public int getPrice() {
            return price;
        }
    }

    public class InitialResources {
        private Vector<Vehicle> vehicles;

        public class Vehicle {
            private int speed;
            private int license;

            public int getSpeed() {
                return speed;
            }

            public int getLicense() {
                return license;
            }
        }

        public Vector<Vehicle> getVehicles() {
            return vehicles;
        }
    }

    public class Services {
        private TimeServ time;
        private int selling;
        private int inventoryService;
        private int logistics;
        private int resourcesService;
        private Cust[] customers;

        public class TimeServ{
            private int speed;
            private int duration;

            public int getSpeed() {
                return speed;
            }

            public int getDuration() {
                return duration;
            }
        }

        public TimeServ getTime() {
            return time;
        }

        public int getSelling() {
            return selling;
        }

        public int getInventoryService() {
            return inventoryService;
        }

        public int getLogistics() {
            return logistics;
        }

        public int getResourcesService() {
            return resourcesService;
        }

        public class Cust {
            private int id;
            private String name;
            private String address;
            private int distance;
            private CreditCard creditCard;
            private List<OrderSchedule> orderSchedule;

            public class CreditCard {
                private int number;
                private int amount;
                public int getNumber() {
                    return number;
                }

                public int getAmount() {
                    return amount;
                }



            }

            public class OrderSchedule {
                private String bookTitle;
                private int tick;

                public String getBookTitle() {
                    return bookTitle;
                }

                public int getTick() {
                    return tick;
                }
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getDistance() {
                return distance;
            }

            public CreditCard getCreditCard() {
                return creditCard;
            }

            public List<OrderSchedule> getOrderSchedule() {
                return orderSchedule;
            }

            public String getAddress() {
                return address;
            }
        }
        public Cust[] getCustomers() {
            return customers;
        }
    }



    public InitialInventory[] getInitialInventory() {
        return initialInventory;
    }

    public List<InitialResources> getInitialResources() {
        return initialResources;
    }

    public Services getServices() {
        return services;
    }


}