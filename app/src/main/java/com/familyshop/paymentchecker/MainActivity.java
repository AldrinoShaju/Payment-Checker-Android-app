package com.familyshop.paymentchecker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.familyshop.paymentchecker.adapter.RecyclerViewCustomerAdapter;
import com.familyshop.paymentchecker.data.Repository;
import com.familyshop.paymentchecker.fragment.BottomSheetCustomerFragment;
import com.familyshop.paymentchecker.models.Customer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewCustomerAdapter.onContactClickListener{

    private RecyclerView recyclerViewCustomer;
    private RecyclerViewCustomerAdapter adapter;

    private List<Customer> customerList = new ArrayList<>();

    private FloatingActionButton addCustomerBtn;

    private BottomSheetCustomerFragment bottomSheetCustomerFragment;

    private ProgressBar progressBar;

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
                progressBar.setVisibility(View.VISIBLE);
                new Repository().getAllCustomers((resp)-> loadCustomerListUI(resp));
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

        recyclerViewCustomer = findViewById(R.id.recycler_view_customer);
        addCustomerBtn = findViewById(R.id.addButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        setupBottomSheetCustFragment();

        addCustomerBtn.setOnClickListener(view -> showBottomCustomerFragment());

        new Repository().getAllCustomers((resp)-> loadCustomerListUI(resp));

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
    }

    @Override
    public void onContactClick(int position) {
        Log.d("List View click", "onContactClick: " + customerList.get(position).getCustId());
        Intent i = new Intent(this, CustomerActivity.class);
        i.putExtra("data", customerList.get(position));
        startActivity(i);
    }

    private void setupBottomSheetCustFragment() {
        bottomSheetCustomerFragment = new BottomSheetCustomerFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.customerBottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
    }
}