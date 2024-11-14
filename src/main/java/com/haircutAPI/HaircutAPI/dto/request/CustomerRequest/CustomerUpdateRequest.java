package com.haircutAPI.HaircutAPI.dto.request.CustomerRequest;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

public class CustomerUpdateRequest {
    private String password;
    private String nameCustomer;
    private double loyaltyPoint;
    private String DoB;
    private String email;
    private String address;
    private String phoneNumber;
    private String startDate;
    private String lastDayUsing;
    private CustomerTypes typeCustomer;
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNameCustomer() {
        return nameCustomer;
    }
    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }
    public double getLoyaltyPoint() {
        return loyaltyPoint;
    }
    public void setLoyaltyPoint(double loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
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

    
}
