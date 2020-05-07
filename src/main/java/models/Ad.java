package models;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Date;

public class Ad {
    private Contact contacts;
    private Date creationDate;
    private int price;
    private String[] publicationOptions;
    private String reference;
    private Vehicle vehicle;


    public Ad(Contact contacts, Date creationDate, int price, String[] publicationOptions, String reference, Vehicle vehicle) {
        this.contacts = contacts;
        this.creationDate = creationDate;
        this.price = price;
        this.publicationOptions = publicationOptions;
        this.reference = reference;
        this.vehicle = vehicle;
    }

    public Ad() {
        super();
    }

    public Contact getContacts() {
        return contacts;
    }

    public void setContacts(Contact contacts) {
        this.contacts = contacts;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String[] getPublicationOptions() {
        return publicationOptions;
    }

    public void setPublicationOptions(String[] publicationOptions) {
        this.publicationOptions = publicationOptions;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
