package com.example.brandonpaw.receipttracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Brandon Paw on 8/14/2017.
 */

class ReceiptViewHolder extends RecyclerView.ViewHolder {

    public TextView receipt;
    public TextView date;
    public TextView total;
    public TextView tip;
    public TextView tax;

    public ReceiptViewHolder(View view) {
        super(view);
        receipt = (TextView) view.findViewById(R.id.receipt_title);
        date = (TextView) view.findViewById(R.id.receipt_date);
        total = (TextView) view.findViewById(R.id.receipt_total);
        tip = (TextView) view.findViewById(R.id.receipt_tip);
        tax = (TextView) view.findViewById(R.id.receipt_tax);
    }
}

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    private final List<Receipt> receipts;

    public ReceiptAdapter(List<Receipt> receipt) {
        this.receipts = receipt;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_receipt, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ...
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {

        Receipt receipt = receipts.get(position);

        holder.receipt.setText(receipt.receipt);
        holder.date.setText(receipt.date);
        holder.total.setText(""+receipt.total);
        holder.tip.setText(""+receipt.tip);
        holder.tax.setText(""+receipt.tax);

        System.out.println("Done Binding");
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
}
