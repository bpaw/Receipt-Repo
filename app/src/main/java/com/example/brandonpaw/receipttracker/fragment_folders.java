package com.example.brandonpaw.receipttracker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_folders extends Fragment {

    RecyclerView mRecyclerView;

    public List<String> folders = new ArrayList<String>();

    public fragment_folders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_folders, container, false);

        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.folder_recycler);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(new FolderAdapter(folders));
        }

//        List<String> folders = HomepageActivity.utilRest.folders;
        UtilREST utilREST = new UtilREST(getActivity());
        if (PersistentDataSingleton.persistentData.user != null && PersistentDataSingleton.persistentData.user.rid != null) {
            utilREST.getFolders(PersistentDataSingleton.persistentData.user.rid).addOnSuccessListener(new OnSuccessListener<JSONArray>() {
                @Override
                public void onSuccess(JSONArray jsonArrayRequest) {
                    int len = jsonArrayRequest.length();
                    if (len > 0) {
                        for (int i = 0; i < len; i++) {
                            try {
                                String folder = jsonArrayRequest.get(i).toString();
                                Log.e("BPAW", "The folder is : " + folder);
                                folders.add(folder);
                                if (folders.isEmpty()) {
                                    Log.e("BPAW", "FOLDERS LIST IS EMPTY");
                                } else {
                                    DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(),
                                            DividerItemDecoration.VERTICAL);
                                    mRecyclerView.addItemDecoration(divider);
                                    mRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        return view;
    }

}
