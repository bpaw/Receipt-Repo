package com.example.brandonpaw.receipttracker;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Brandon Paw on 8/15/2017.
 */

class FolderViewHolder extends RecyclerView.ViewHolder {

    final TextView folderName;
    final Button folderButton;

    public FolderViewHolder(View view) {
        super(view);
        folderName = (TextView) view.findViewById(R.id.folder_name);
        folderButton = (Button) view.findViewById(R.id.folder_button);
    }
}

public class FolderAdapter extends RecyclerView.Adapter<FolderViewHolder> {

    private Activity activity;
    private List<String> folders;

    public FolderAdapter(List<String> folders, Activity activity) {
        this.activity = activity;
        this.folders = folders;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FolderViewHolder holder, final int position) {

        holder.folderName.setText(folders.get(position));
        holder.folderButton.setBackgroundColor(Color.parseColor("#009688"));
        holder.folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), folders.get(position), Toast.LENGTH_LONG).show();
                UtilREST util = new UtilREST(activity);
                util.getFolderReceipts(PersistentDataSingleton.persistentData.user.rid, holder.folderName.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            Log.e("BPAW", "the JSONObject is " + jsonObject.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BPAW", "EXCEPTION : " + e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
