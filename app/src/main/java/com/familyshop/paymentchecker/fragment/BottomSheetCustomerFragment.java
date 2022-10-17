package com.familyshop.paymentchecker.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.familyshop.paymentchecker.CustomerActivity;
import com.familyshop.paymentchecker.R;
import com.familyshop.paymentchecker.data.Repository;
import com.familyshop.paymentchecker.models.CustomerRequest;
import com.familyshop.paymentchecker.models.TransactionRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetCustomerFragment extends BottomSheetDialogFragment {

    private EditText custName;
    private EditText phoneNumber;

    private Button saveCustomerBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_bottom_sheet, container, false);
        custName = view.findViewById(R.id.editText_cust_name_field);
        phoneNumber = view.findViewById(R.id.editText_phone_num_field);

        saveCustomerBtn = view.findViewById(R.id.add_cust_btn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveCustomerBtn.setOnClickListener(view1 -> {
            Log.d(TAG, "onViewCreated: " + "save customer button clicked");
            String customerName = custName.getText().toString();
            String phoneNumber = this.phoneNumber.getText().toString();

            CustomerRequest request = new CustomerRequest(customerName, phoneNumber);
            new Repository().saveCustomerDetails(response -> {
                int status = response.getInt("status");
                if(status==201) {
                    Toast.makeText(getContext(), "Txn Updated, Pls refresh", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error occurred: "+status, Toast.LENGTH_SHORT).show();
                }
            }, request);

        });
    }
}
