package com.familyshop.paymentchecker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.familyshop.paymentchecker.R;
import com.familyshop.paymentchecker.models.Customer;

import java.util.List;

public class RecyclerViewCustomerAdapter extends RecyclerView.Adapter<RecyclerViewCustomerAdapter.ViewHolder>{

    private onContactClickListener contactClickListener;
    private List<Customer> customerInfoList;
    private Context context;

    public RecyclerViewCustomerAdapter(List<Customer> customerInfoList, Context context, onContactClickListener contactClickListener) {
        this.customerInfoList = customerInfoList;
        this.context = context;
        this.contactClickListener = contactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, contactClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customerInfo = customerInfoList.get(position);

        holder.customerName.setText(customerInfo.getCustName());
        holder.balancePayment.setText(String.valueOf(customerInfo.getTotalPayable()));
    }

    @Override
    public int getItemCount() {
        return customerInfoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onContactClickListener onContactClickListener;
        public TextView customerName;
        public TextView balancePayment;
        public ViewHolder(@NonNull View itemView, onContactClickListener onContactClickListener) {
            super(itemView);

            customerName = itemView.findViewById(R.id.textView_name_value);
            balancePayment = itemView.findViewById(R.id.textView_bal_value);
            this.onContactClickListener = onContactClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactClickListener.onContactClick(getAdapterPosition());
        }
    }

    public interface onContactClickListener {
        void onContactClick(int position);
    }
}
