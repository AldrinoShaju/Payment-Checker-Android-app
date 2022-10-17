package com.familyshop.paymentchecker.data;

import org.json.JSONException;
import org.json.JSONObject;

public interface AsyncResponse {

    void processFinished(JSONObject customerList) throws JSONException;
}
