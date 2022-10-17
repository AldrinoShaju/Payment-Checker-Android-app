package com.familyshop.paymentchecker.models;

public class CustomerRequest {

    private String custName;
    private String phoneNumber;

    public CustomerRequest(String custName, String phoneNumber) {
        this.custName = custName;
        this.phoneNumber = phoneNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
