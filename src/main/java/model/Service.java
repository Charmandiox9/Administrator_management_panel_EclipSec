package model;

import java.sql.Timestamp;

public class Service {
    private int id;
    private String name;
    private String description;
    private double price;
    private boolean active;
    private Timestamp created_at;

    public Service(int id, String name, String description, double price, boolean active, Timestamp created_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
