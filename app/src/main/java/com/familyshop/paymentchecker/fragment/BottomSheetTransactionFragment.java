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
import com.familyshop.paymentchecker.models.TransactionRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetTransactionFragment extends BottomSheetDialogFragment {

    private EditText totalAmount;
    private EditText payedAmount;
    private EditText txnNote;
    private Button updateTxn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_bottom_sheet, container, false);
        totalAmount = view.findViewById(R.id.editText_total_amount_field);
        payedAmount = view.findViewById(R.id.editText_cust_name_field);
        txnNote = view.findViewById(R.id.editText_phone_num_field);

        updateTxn = view.findViewById(R.id.button_update_txn);

        totalAmount.setText(String.valueOf(CustomerActivity.data.getTxnList().get(CustomerActivity.currTxnPosition).getTotalAmount()));
//        payedAmount.setText(CustomerActivity.data.getTxnList().get(CustomerActivity.currTxnPosition).getPayable());
        txnNote.setText(CustomerActivity.data.getTxnList().get(CustomerActivity.currTxnPosition).getTxnNote());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateTxn.setOnClickListener(view1 -> {
            Log.d(TAG, "onViewCreated: " + "update txn button clicked");
            int totalAm = Integer.valueOf(totalAmount.getText().toString());
            int payedAm = Integer.valueOf(payedAmount.getText().toString());
            String note = txnNote.getText().toString().trim();

            TransactionRequest request = new TransactionRequest(note, totalAm, payedAm);
            new Repository().updateTransaction(response -> {
                int status = response.getInt("status");
                if(status==201) {
                    Toast.makeText(getContext(), "Txn Updated, Pls refresh", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error occurred: "+status, Toast.LENGTH_SHORT).show();
                }
            }, request, CustomerActivity.data.getCustId(), CustomerActivity.data.getTxnList().get(CustomerActivity.currTxnPosition).getTxnId());

        });
    }
}
