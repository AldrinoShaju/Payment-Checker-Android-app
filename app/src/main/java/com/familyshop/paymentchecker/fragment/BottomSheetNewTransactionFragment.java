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
import com.familyshop.paymentchecker.models.TransactionRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetNewTransactionFragment extends BottomSheetDialogFragment {

    private EditText totalAmount;
    private EditText payedAmount;
    private EditText txnNote;
    private Button addNewTxn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_bottom_sheet_newtxn, container, false);
        totalAmount = view.findViewById(R.id.editText_new_total_amount);
        payedAmount = view.findViewById(R.id.editText_new_Payed_Amount);
        txnNote = view.findViewById(R.id.editText_note);
        addNewTxn = view.findViewById(R.id.button2);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addNewTxn.setOnClickListener(view1 -> {

            int totalAmount = Integer.valueOf(this.totalAmount.getText().toString());
            int payed= Integer.valueOf(payedAmount.getText().toString());
            String txnNote = this.txnNote.getText().toString().trim();
            TransactionRequest request = new TransactionRequest(txnNote, totalAmount, payed);
            new Repository().addTransaction(response ->{
                int status = response.getInt(STATUS);
                if(status==201) {
                    Toast.makeText(getContext(), "Added new Txn", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Failed to add: "+status, Toast.LENGTH_SHORT).show();
                }
            }, request, CustomerActivity.data.getCustId());

        });

    }
}
