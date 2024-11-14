package com.haircutAPI.HaircutAPI.enity;


import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String password;
    private String nameCustomer;
    private double loyaltyPoint;
    private String DoB;
    private String email;
    private String address;
    private String phoneNumber;
    private String startDate;
    private String lastDayUsing;

    @Enumerated(EnumType.ORDINAL)
    private CustomerTypes typeCustomer;

    

    public String getLastDayUsing() {
        return lastDayUsing;
    }

    public void setLastDayUsing(String lastDayUsing) {
        this.lastDayUsing = lastDayUsing;
    }

    public CustomerTypes getTypeCustomer() {
        return typeCustomer;
    }

    public void setTypeCustomer(CustomerTypes typeCustomer) {
        this.typeCustomer = typeCustomer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameWorker) {
        this.nameCustomer = nameWorker;
    }

    public double getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(double salary) {
        this.loyaltyPoint = salary;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
