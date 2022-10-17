package com.familyshop.paymentchecker.models;


import java.io.Serializable;
import java.util.List;

public class Customer implements Serializable {
    private String custId;
    private String custName;
    private String phoneNumber;
    private String createdOn;
    private Integer totalPayable;
    private List<Transaction> txnList;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Integer getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(Integer totalPayable) {
        this.totalPayable = totalPayable;
    }

    public List<Transaction> getTxnList() {
        return txnList;
    }

    public void setTxnList(List<Transaction> txnList) {
        this.txnList = txnList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId='" + custId + '\'' +
                ", custName='" + custName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", totalPayable=" + totalPayable +
                ", txnList=" + txnList +
                '}';
    }
}
