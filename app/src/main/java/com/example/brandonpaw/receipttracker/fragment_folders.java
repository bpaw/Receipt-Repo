package com.example.brandonpaw.receipttracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_folders extends Fragment {

    RecyclerView mRecyclerView;

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
        }

        List<String> folders = HomepageActivity.utilRest.folders;

        if (folders.isEmpty()) {
            Log.e("BPAW", "FOLDERS LIST IS EMPTY");
        }
        else {
            DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            mRecyclerView.addItemDecoration(divider);
            mRecyclerView.setAdapter(new FolderAdapter(folders));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

        return view;
    }

}
