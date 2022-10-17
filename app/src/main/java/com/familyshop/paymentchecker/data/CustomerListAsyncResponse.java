package com.familyshop.paymentchecker.data;

import com.familyshop.paymentchecker.models.Customer;

import java.util.List;

public interface CustomerListAsyncResponse {

    void processFinished(List<Customer> customerList);

//    void processFinishedGetSingleCusomer(CustomerData customerData);
}
