package com.familyshop.paymentchecker;

import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.DATA;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.MESSAGE;
import static com.familyshop.paymentchecker.constants.PaymentCheckConstants.STATUS;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.familyshop.paymentchecker.adapter.RecyclerViewCustomerAdapter;
import com.familyshop.paymentchecker.data.Repository;
import com.familyshop.paymentchecker.fragment.BottomSheetCustomerFragment;
import com.familyshop.paymentchecker.models.Customer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewCustomerAdapter.onContactClickListener{

    private RecyclerView recyclerViewCustomer;
    private RecyclerViewCustomerAdapter adapter;

    private List<Customer> customerList = new ArrayList<>();

    private FloatingActionButton addCustomerBtn;

    private BottomSheetCustomerFragment bottomSheetCustomerFragment;

    private ProgressBar progressBar;

    private TextView paymentIn;
    private TextView paymentOut;

    private CardView paymentSummary;

    private int totalPayIn = 0;
    private int totalPayOut = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_btn:
                loadCustomerListUI(new ArrayList<Customer>());
                paymentSummary.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                new Repository().getAllCustomers((resp)-> convertResponseToCustomerList(resp));
                Toast.makeText(this, "Refreshing Customer List", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar customToolBar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(customToolBar);

        paymentIn = findViewById(R.id.textView_pay_in_edit);
        paymentOut = findViewById(R.id.textView_pay_out_edit);

        paymentSummary = findViewById(R.id.cardView_summary);
        paymentSummary.setVisibility(View.INVISIBLE);

        recyclerViewCustomer = findViewById(R.id.recycler_view_customer);
        addCustomerBtn = findViewById(R.id.addButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        setupBottomSheetCustFragment();

        addCustomerBtn.setOnClickListener(view -> showBottomCustomerFragment());

        new Repository().getAllCustomers(response -> {
            convertResponseToCustomerList(response);
        });

    }

    private void showBottomCustomerFragment() {
        bottomSheetCustomerFragment.show(getSupportFragmentManager(), bottomSheetCustomerFragment.getTag());
    }

    private void loadCustomerListUI(List<Customer> customerList) {
        this.customerList = customerList;
        recyclerViewCustomer.setHasFixedSize(true);
        recyclerViewCustomer.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewCustomerAdapter(customerList, this, this);
        recyclerViewCustomer.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        getTotalPaymentSummary();
        paymentIn.setText(String.valueOf(totalPayIn));
        paymentOut.setText(String.valueOf(totalPayOut));

        paymentSummary.setVisibility(View.VISIBLE);
    }

    @Override
    public void onContactClick(int position) {
        Intent i = new Intent(this, CustomerActivity.class);
        i.putExtra(DATA, customerList.get(position));
        startActivity(i);
    }

    private void setupBottomSheetCustFragment() {
        bottomSheetCustomerFragment = new BottomSheetCustomerFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.customerBottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void convertResponseToCustomerList(JSONObject response) {
        List<Customer> customerList = new ArrayList<>();
        try {
            if(response.getInt(STATUS)==200) {
                JSONArray allCustomerArray = response.getJSONArray(MESSAGE);
                for(int i=0;i<allCustomerArray.length();i++) {
                    customerList.add((new Gson()).fromJson(allCustomerArray.getString(i), Customer.class));
                }
            }else {
                Toast.makeText(this, "Get Customer List failed, Try again later", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadCustomerListUI(customerList);
    }

    private void getTotalPaymentSummary() {
        if(!customerList.isEmpty()) {
            totalPayIn = customerList.stream().filter(cust->cust.getTotalPayable()>0).mapToInt(customer -> customer.getTotalPayable()).sum();
            totalPayOut = customerList.stream().filter(cust->cust.getTotalPayable()<0).mapToInt(customer -> customer.getTotalPayable()).sum();
        }
    }


}