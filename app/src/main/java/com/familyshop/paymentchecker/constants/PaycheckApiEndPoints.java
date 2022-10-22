package com.familyshop.paymentchecker.constants;

public class PaycheckApiEndPoints {

    public static final String ENV_STATUS = "https://paycheck-api.herokuapp.com/status/env";
    public static final String GET_ALL_CUSTOMERS = "https://paycheck-api.herokuapp.com/customer";
    public static final String ADD_CUSTOMERS = "https://paycheck-api.herokuapp.com/customer";
    public static final String ADD_TXN = "https://paycheck-api.herokuapp.com/txn?id=";
    public static final String GET_CUSTOMER = "https://paycheck-api.herokuapp.com/customer/get?id=";
    public static final String UPDATE_TXN = "https://paycheck-api.herokuapp.com/txn?id=";
    public static final String PAY_TXN = "https://paycheck-api.herokuapp.com/txn/pay?";
    public static final String SETTLE_TXN = "https://paycheck-api.herokuapp.com/txn/settle?";

}
