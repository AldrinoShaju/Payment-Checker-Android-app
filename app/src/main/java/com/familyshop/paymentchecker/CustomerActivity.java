package com.familyshop.paymentchecker;

import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.DATA;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.MESSAGE;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.STATUS;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.familyshop.paymentchecker.adapter.RecyclerViewTransactionAdapter;
import com.familyshop.paymentchecker.data.Repository;
import com.familyshop.paymentchecker.fragment.BottomSheetNewTransactionFragment;
import com.familyshop.paymentchecker.fragment.BottomSheetPayTransactionFragment;
import com.familyshop.paymentchecker.fragment.BottomSheetSettleTransactionFragment;
import com.familyshop.paymentchecker.models.Customer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.Collections;

public class CustomerActivity extends AppCompatActivity implements RecyclerViewTransactionAdapter.onContactClickListener, RecyclerViewTransactionAdapter.onLongContactClickListener{

    private TextView customerName;
    private TextView phoneNumber;
    private TextView totalPayable;

    private FloatingActionButton addTxnBtn;
    private Button settleBtn;

    private RecyclerView recyclerViewTxn;
    private RecyclerViewTransactionAdapter adapter;

    public static Customer data = new Customer();

    private ProgressBar progressLoading;

    private BottomSheetNewTransactionFragment bottomSheetNewTransactionFragment;
    private BottomSheetPayTransactionFragment bottomSheetPayTransactionFragment;
    private BottomSheetSettleTransactionFragment bottomSheetSettleTransactionFragment;

    public static int currTxnPosition = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                progressLoading.setVisibility(View.VISIBLE);
                new Repository().getSingleCustomerDetails((resp)-> {
                    int status = resp.getInt(STATUS);
                    if(status==200) {
                        data = (new Gson()).fromJson(resp.getString(MESSAGE), Customer.class);
                        loadTransactionListUI(data);
                    }else {

                    }
                }, data.getCustId());
                Toast.makeText(this, "Refreshing Txn List", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar customToolBar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolBar);

        progressLoading = findViewById(R.id.progressBarTxn);
        progressLoading.setVisibility(View.VISIBLE);

        customerName = findViewById(R.id.textView_custName_field);
        phoneNumber = findViewById(R.id.textView_phoneNum_field);
        totalPayable = findViewById(R.id.textView_rem_pay_field);

        addTxnBtn = findViewById(R.id.floatingActionButton_txn);
        settleBtn = findViewById(R.id.btn_settle_payment);
        recyclerViewTxn = findViewById(R.id.recycler_view_txn);

        Intent intent = getIntent();
        data = (Customer) intent.getSerializableExtra(DATA);

        customerName.setText(data.getCustName());
        phoneNumber.setText(data.getPhoneNumber());
        totalPayable.setText(String.valueOf(data.getTotalPayable()));

        loadTransactionListUI(data);

        bottomSheetPayTransactionFragment = new BottomSheetPayTransactionFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.payTxnBottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetNewTransactionFragment = new BottomSheetNewTransactionFragment();
        ConstraintLayout constraintLayout1 = findViewById(R.id.transactionBottomSheetNewTxn);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior1 = BottomSheetBehavior.from(constraintLayout1);
        bottomSheetBehavior1.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetSettleTransactionFragment = new BottomSheetSettleTransactionFragment();
        ConstraintLayout constraintLayout2 = findViewById(R.id.settleTxnBottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior2 = BottomSheetBehavior.from(constraintLayout2);
        bottomSheetBehavior2.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        addTxnBtn.setOnClickListener(view -> {
            bottomSheetNewTransactionFragment.show(getSupportFragmentManager(), bottomSheetNewTransactionFragment.getTag());
        });

        settleBtn.setOnClickListener(view -> {
            bottomSheetSettleTransactionFragment.show(getSupportFragmentManager(), bottomSheetSettleTransactionFragment.getTag());
        });
    }

    private void loadTransactionListUI(Customer customer) {
        progressLoading.setVisibility(View.GONE);
        this.data = customer;
        totalPayable.setText(String.valueOf(data.getTotalPayable()));
        Collections.reverse(data.getTxnList());
        recyclerViewTxn.setHasFixedSize(true);
        recyclerViewTxn.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewTransactionAdapter(data.getTxnList(), this, this, this);
        recyclerViewTxn.setAdapter(adapter);
    }

    @Override
    public void onContactClick(int position) {
        currTxnPosition = position;
        bottomSheetPayTransactionFragment.show(getSupportFragmentManager(), bottomSheetPayTransactionFragment.getTag());
    }

    @Override
    public boolean onLongContactClick(int position) {
        Toast.makeText(this, "Long Click Detected", Toast.LENGTH_SHORT).show();
        showAlertDeleteBox(position);
        return true;
    }

    private void showAlertDeleteBox(int pos) {
        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Do you want to delete this transaction?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            new Repository().deleteTransaction(resp->{
                int status = resp.getInt("status");
                if(status==201) {
                    Toast.makeText(this, "Txn Deleted, refresh page", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }, data.getCustId(), data.getTxnList().get(pos).getTxnId());
        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}