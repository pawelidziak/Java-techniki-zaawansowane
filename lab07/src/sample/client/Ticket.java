package sample.client;

import java.io.Serializable;

public class Ticket implements Serializable{
    private String categoryName;
    private int number;
    private int priority;

    public Ticket(String categoryName, int number, int priority) {
        this.categoryName = categoryName;
        this.number = number;
        this.priority = priority;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return categoryName.substring(0,1) + number;
    }
}
