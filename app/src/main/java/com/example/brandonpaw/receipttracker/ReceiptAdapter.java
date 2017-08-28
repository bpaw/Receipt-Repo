package com.example.brandonpaw.receipttracker;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by Brandon Paw on 8/14/2017.
 */

class ReceiptViewHolder extends RecyclerView.ViewHolder {

    public TextView receipt;
    public TextView date;
    public TextView total;
    public ImageView photo;
    public TextView tip;
    public TextView tax;

    public ReceiptViewHolder(View view) {
        super(view);
        receipt = (TextView) view.findViewById(R.id.receipt_title);
        date = (TextView) view.findViewById(R.id.receipt_date);
        total = (TextView) view.findViewById(R.id.receipt_total);
//        tip = (TextView) view.findViewById(R.id.receipt_tip);
//        tax = (TextView) view.findViewById(R.id.receipt_tax);
        photo = (ImageView) view.findViewById(R.id.receipt_photo);
    }
}

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    private Activity activity;
    private final List<Receipt> receipts;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;

    public ReceiptAdapter(List<Receipt> receipt, Activity activity) {

        this.receipts = receipt;
        this.activity = activity;
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReference();
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
    public void onBindViewHolder(final ReceiptViewHolder holder, int position) {

        Receipt receipt = receipts.get(position);

        holder.receipt.setText(receipt.receipt);
        holder.date.setText(receipt.date);
        holder.total.setText(""+receipt.total);
//        holder.tip.setText(""+receipt.tip);
//        holder.tax.setText(""+receipt.tax);

        // grab photo from Firebase Storage if it exists
        Log.e("RECEIPT ADDRESS", "user/" + Long.toString(receipt.owner.rid) + "/" + Long.toString(receipt.rid));
//        StorageReference  userRef = storageRef.child("user")
//                .child(Long.toString(receipt.owner.rid))
//                .child(Long.toString(receipt.rid));
//        Task<Uri> downloadURL = userRef.getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        if (uri != null) {
//                            Log.e("RECEIPT LOADING", "An image came back NON-NULL");
//                        Picasso
//                                .with(activity.getBaseContext())
//                                .load(uri)
//                                .into(holder.photo);
//                       }
//
//                    }})
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("BPAW","FAILURE TRYING TO DOWNLOAD URL FROM FIREBASE" + e.getMessage());
//                    }
//                });
        System.out.println("Done Binding");
    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }
}
