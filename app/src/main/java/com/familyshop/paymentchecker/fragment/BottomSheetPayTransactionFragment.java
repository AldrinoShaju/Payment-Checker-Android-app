package com.familyshop.paymentchecker.fragment;

import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.STATUS;

import android.os.Bundle;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetPayTransactionFragment extends BottomSheetDialogFragment {

    private EditText payedAmount;

    private Button updateTxn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_bottom_sheet_pay, container, false);
        payedAmount = view.findViewById(R.id.editText_total_amount_field);

        updateTxn = view.findViewById(R.id.button_update_txn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateTxn.setOnClickListener(view1 -> {

            int totalAm = Integer.valueOf(payedAmount.getText().toString());

            new Repository().payTransaction(response -> {
                int status = response.getInt(STATUS);
                if(status==201) {
                    Toast.makeText(getContext(), "Txn payed, Pls refresh", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Error occurred: "+status, Toast.LENGTH_SHORT).show();
                }
            }, totalAm, CustomerActivity.data.getCustId(), CustomerActivity.data.getTxnList().get(CustomerActivity.currTxnPosition).getTxnId());

        });
    }
}
