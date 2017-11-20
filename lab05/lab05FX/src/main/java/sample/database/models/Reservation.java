package sample.database.models;

import java.util.List;

public class Reservation {
    public static final String STATUS_OPEN = "open";
    public static final String STATUS_CANCELED = "canceled";
    private int id;
    private Client client;
    private List<Service> services;
    private int bookedHour;
    private String status;
    private double total_price;

    public Reservation() {
    }

    public Reservation(int id, Client client, List<Service> services, int bookedHour, String status) {
        this.id = id;
        this.client = client;
        this.services = services;
        this.bookedHour = bookedHour;
        this.status = status;
    }

    public double getPrice() {
        double price = 0;
        if (services != null) {
            for (Service service : services) {
                price += service.getPrice();
            }
        }
        return price;
    }


    public void setPrice(double total_price) {
        this.total_price = total_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public int getBookedHour() {
        return bookedHour;
    }

    public void setBookedHour(int bookedHour) {
        this.bookedHour = bookedHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                ", CLIENT=" + client +
                ", SERVICES=" + services +
                ", BOOKED_HOUR=" + bookedHour +
                ", STATUS='" + status +
                ", TOTAL_PRICE='" + total_price +
                '\'';
    }
}
