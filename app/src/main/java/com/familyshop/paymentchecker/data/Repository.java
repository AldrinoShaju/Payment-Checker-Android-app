package com.familyshop.paymentchecker.data;

import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.AND_CUSTID;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.AND_PAYED_AMOUNT;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.AND_PAYED_AMT;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.AND_TXN_ID;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.CUST_ID;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.familyshop.paymentchecker.constants.PaycheckApiEndPoints;
import com.familyshop.paymentchecker.controller.AppController;
import com.familyshop.paymentchecker.models.Customer;
import com.familyshop.paymentchecker.models.CustomerRequest;
import com.familyshop.paymentchecker.models.TransactionRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    List<Customer> customerList = new ArrayList<>();
    Customer customer = new Customer();

    public List<Customer> getAllCustomers(final AsyncResponse callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                PaycheckApiEndPoints.GET_ALL_CUSTOMERS,
                null, resp->{


            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return customerList;
    }

    public void saveCustomerDetails(final AsyncResponse callback, CustomerRequest saveCustomerDetails) {

        String jsonInString = new Gson().toJson(saveCustomerDetails);
        JSONObject mJSONObject = null;
        try {
            mJSONObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                PaycheckApiEndPoints.ADD_CUSTOMERS,
                mJSONObject, resp->{
            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void addTransaction(final AsyncResponse callback, TransactionRequest request, String custId) {

        String jsonInString = new Gson().toJson(request);
        JSONObject mJSONObject = null;

        try {
            mJSONObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder sbEndpointURL = new StringBuilder(PaycheckApiEndPoints.ADD_TXN);
        sbEndpointURL.append(custId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                sbEndpointURL.toString(),
                mJSONObject, resp->{

            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
//
    public JSONObject getSingleCustomerDetails(final AsyncResponse callback, String custId) {

        StringBuilder sbEndpointURL = new StringBuilder(PaycheckApiEndPoints.GET_CUSTOMER);
        sbEndpointURL.append(custId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                sbEndpointURL.toString(),
                null, resp->{

            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return new JSONObject();
    }

    public void updateTransaction(final AsyncResponse callback, TransactionRequest request, String custId, String txnId) {

        String jsonInString = new Gson().toJson(request);
        JSONObject mJSONObject = null;

        try {
            mJSONObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder sbEndpointURL = new StringBuilder(PaycheckApiEndPoints.UPDATE_TXN);
        sbEndpointURL.append(txnId);
        sbEndpointURL.append(AND_CUSTID);
        sbEndpointURL.append(custId);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,
                sbEndpointURL.toString(),
                mJSONObject, resp->{

            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void payTransaction(final AsyncResponse callback, int payAmount, String custId, String txnId) {


        StringBuilder sbEndpointURL = new StringBuilder(PaycheckApiEndPoints.PAY_TXN);
        sbEndpointURL.append(CUST_ID);
        sbEndpointURL.append(custId);
        sbEndpointURL.append(AND_TXN_ID);
        sbEndpointURL.append(txnId);
        sbEndpointURL.append(AND_PAYED_AMT);
        sbEndpointURL.append(payAmount);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,
                sbEndpointURL.toString(),
                null, resp->{

            if(callback!=null){
                try {
                    callback.processFinished(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void settleTransactions(final AsyncResponse callback, int settleAmt, String custId) {

        StringBuilder sbEndPointURL = new StringBuilder(PaycheckApiEndPoints.SETTLE_TXN);
        sbEndPointURL.append(CUST_ID);
        sbEndPointURL.append(custId);
        sbEndPointURL.append(AND_PAYED_AMOUNT);
        sbEndPointURL.append(settleAmt);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH,
                sbEndPointURL.toString(),
                null, response -> {

            if(callback!=null) {
                try {
                    callback.processFinished(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, Exception::printStackTrace);

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
