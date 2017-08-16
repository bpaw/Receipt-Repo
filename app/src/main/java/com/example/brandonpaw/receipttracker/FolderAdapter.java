package com.example.brandonpaw.receipttracker;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    private List<String> folders;

    public FolderAdapter(List<String> folders) {
        this.folders = folders;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, final int position) {

        holder.folderName.setText(folders.get(position));
        holder.folderButton.setBackgroundColor(Color.parseColor("#009688"));
        holder.folderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), folders.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
