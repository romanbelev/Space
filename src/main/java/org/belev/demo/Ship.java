package org.belev.demo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ship implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String direction;
    private int price;
    private int seats;
    private String departureDate;
    private String arrivalDate;

    @OneToMany(mappedBy="ship", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tourist> tourists = new ArrayList<Tourist>();

    private Ship() {}

    public Ship(String direction, int price, int seats, String departureDate, String arrivalDate) {
        this.direction = direction;
        this.price = price;
        this.seats = seats;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public void addTourist(Tourist tourist) {
        tourist.setShip(this);
        tourists.add(tourist);
    }

    public Long getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    public int getPrice() {
        return price;
    }

    public int getSeats() {
        return seats;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String toString() {
        return "Ship: " +
                "\nid: " + id +
                "\ndirection: " + direction +
                "\nprice: " + price +
                "\nseats: " + seats +
                "\ndepartureDate: " + departureDate +
                "\narrivalDate: " + arrivalDate +
                "\ntourists: " + tourists;
    }
}