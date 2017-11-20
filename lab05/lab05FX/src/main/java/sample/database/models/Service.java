package sample.database.models;

public class Service {
    private int id;
    private String name;
    private double price;
    private int wheel_size;

    public Service() {
    }

    public Service(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        if (getWheel_size() > 0) return price * (0.1 * getWheel_size());
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWheel_size() {
        return wheel_size;
    }

    public void setWheel_size(int wheel_size) {
        this.wheel_size = wheel_size;
    }

    @Override
    public String toString() {
        return "ID=" + id +
                ", NAME='" + name + '\'' +
                ", PRICE=" + price;
    }
}
