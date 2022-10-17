package com.familyshop.paymentchecker.models;

public class TransactionRequest {
    private String txnNote;
    private Integer totalAmount;
    private Integer payed;

    public TransactionRequest(String txnNote, Integer totalAmount, Integer payed) {
        this.txnNote = txnNote;
        this.totalAmount = totalAmount;
        this.payed = payed;
    }
}
