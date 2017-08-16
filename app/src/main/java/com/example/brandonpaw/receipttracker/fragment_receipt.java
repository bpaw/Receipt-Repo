package com.example.brandonpaw.receipttracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_receipt extends Fragment {

    private RecyclerView mRecyclerView;


    public fragment_receipt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.receipt_recycler);
            if (mRecyclerView != null)
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            else
                System.out.println("mRecyclerView is null");
        }

        UtilREST util = new UtilREST(getActivity());
        List<Receipt> recList2 = HomepageActivity.utilRest.receipts;

        if (recList2.isEmpty())
            Toast.makeText(getContext(), "FAILED TO RETRIEVE RECEIPTS", Toast.LENGTH_LONG).show();
        else {
            DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            mRecyclerView.addItemDecoration(divider);
            mRecyclerView.setAdapter(new ReceiptAdapter(recList2));
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
        return view;
    }

    public void close() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
