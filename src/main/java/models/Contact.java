package models;

import java.util.List;
import java.util.function.Predicate;

public class Contact {
    private String firstName;
    private String lastName;
    private String email;
    private Phone1 phone1;

    public Contact(String firstName, String lastName, String email, Phone1 phone1) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone1 = phone1;
    }

    public Contact() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone1 getPhone1() {
        return phone1;
    }

    public void setPhone1(Phone1 phone1) {
        this.phone1 = phone1;
    }
}
