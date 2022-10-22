package com.familyshop.paymentchecker.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.familyshop.paymentchecker.constants.PaycheckApiEndPoints;
import com.familyshop.paymentchecker.controller.AppController;
import com.familyshop.paymentchecker.models.Customer;
import com.familyshop.paymentchecker.models.CustomerRequest;
import com.familyshop.paymentchecker.models.TransactionRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    List<Customer> customerList = new ArrayList<>();
    Customer customer = new Customer();

    public List<Customer> getAllCustomers(final CustomerListAsyncResponse callback){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PaycheckApiEndPoints.GET_ALL_CUSTOMERS, null, resp->{
            try {
                if(resp.getInt("status")==200) {
                    JSONArray allCustomerArray = resp.getJSONArray("message");
                    for(int i=0;i<allCustomerArray.length();i++) {
                        customerList.add((new Gson()).fromJson(allCustomerArray.getString(i), Customer.class));
                    }
                }else {
                    Log.d("getCustomerContributer", "error response: "+resp.getString("message"));
                }

                if(callback!=null){
                    callback.processFinished(customerList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PaycheckApiEndPoints.ADD_CUSTOMERS, mJSONObject, resp->{
            Log.d("HomeScreen", "saveCustomerDetails: " + resp.toString());

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PaycheckApiEndPoints.ADD_TXN+custId, mJSONObject, resp->{
            Log.d("HomeScreen", "addTransaction: " + resp.toString());

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PaycheckApiEndPoints.GET_CUSTOMER + custId, null, resp->{
            Log.d("HomeScreen", "getSingleCustomer: " + resp.toString());

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, PaycheckApiEndPoints.UPDATE_TXN+txnId+"&custId="+custId, mJSONObject, resp->{
            Log.d("HomeScreen", "updateTransaction: " + resp.toString());

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

        //https://paycheck-api.herokuapp.com/txn/pay?custId=634c06c9258d741cc40c3322&txnId=2&payed=20

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, PaycheckApiEndPoints.PAY_TXN+"custId="+custId+"&txnId="+txnId+"&payed="+payAmount, null, resp->{
            Log.d("HomeScreen", "payTransaction: " + resp.toString());

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

        //eg: https://paycheck-api.herokuapp.com/txn/settle?custId=123&payedAmount=12
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, PaycheckApiEndPoints.SETTLE_TXN+"custId="+custId+"&payedAmount="+settleAmt, null, response -> {
            Log.d("settleTxn", "resp: "+response.toString());

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
