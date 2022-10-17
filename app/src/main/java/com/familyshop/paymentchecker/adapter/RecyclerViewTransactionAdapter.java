package com.familyshop.paymentchecker.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.familyshop.paymentchecker.R;
import com.familyshop.paymentchecker.models.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

public class RecyclerViewTransactionAdapter extends RecyclerView.Adapter<RecyclerViewTransactionAdapter.ViewHolder>{

    private onContactClickListener onContactClickListener;
    private List<Transaction> transactionList;
    private Context context;

    public RecyclerViewTransactionAdapter(List<Transaction> transactionList, Context context, onContactClickListener onContactClickListener) {
        this.transactionList = transactionList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onContactClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        Log.d(TAG, "onBindViewHolder: "+transaction.toString());

        holder.note.setText(transaction.getTxnNote()!=null?transaction.getTxnNote():"");
        holder.totalAmount.setText(String.valueOf(transaction.getTotalAmount()));
        holder.remaining.setText(String.valueOf(transaction.getPayable()));
        int pendingPay = transaction.getPayable();
        if(pendingPay==0) {
            holder.remaining.setTextColor(Color.GREEN);
        }else if(pendingPay<0) {
            holder.remaining.setTextColor(Color.BLUE);
        }

        String createdOn = transaction.getCreatedOn();
        long milliSec = Long.valueOf(createdOn);
        DateFormat simple = new SimpleDateFormat("d MMM yyyy, HH:MMa");
        Date result = new Date(milliSec);
        holder.createdDate.setText(simple.format(result));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        onContactClickListener onContactClickListener;
        public TextView note;
        public TextView totalAmount;
        public TextView remaining;
        public TextView createdDate;
        public ViewHolder(@NonNull View itemView, onContactClickListener onContactClickListener) {
            super(itemView);

            note = itemView.findViewById(R.id.textView_txn_note_field);
            totalAmount = itemView.findViewById(R.id.textView_txn_totalAmount_field);
            remaining = itemView.findViewById(R.id.textView_txn_payable_field);
            createdDate = itemView.findViewById(R.id.textView_txn_date_field);
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
