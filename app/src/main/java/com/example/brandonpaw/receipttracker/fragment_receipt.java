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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.brandonpaw.receipttracker.MainActivity.user;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_receipt extends Fragment {

    private RecyclerView mRecyclerView;

    public List<Receipt> receipt_list = new ArrayList<>();

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
            if (mRecyclerView != null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(new ReceiptAdapter(receipt_list));
            }
            else
                System.out.println("mRecyclerView is null");
        }

        UtilREST util = new UtilREST(getActivity());
        if (PersistentDataSingleton.persistentData.user != null &&
                PersistentDataSingleton.persistentData.user.rid != null) {
            util.getAllReceipts(PersistentDataSingleton.persistentData.user.rid).addOnSuccessListener(new OnSuccessListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        JSONArray jsons = jsonObject.getJSONArray("receipts");
                        if (jsons != null) {
                            Log.e("BPAW", "JSON ARRAY LENGTH : " + jsons.length());
                            for (int i = 0; i < jsons.length(); i++) {
                                Log.e("BPAW", Receipt.JSONtoReceipt(jsons.getJSONObject(i)).toString());
                                receipt_list.add(Receipt.JSONtoReceipt(jsons.getJSONObject(i)));
                            }
                            Log.e("BPAW", "NOW LENGTH IS --- " + receipt_list.size());

                            if (receipt_list.isEmpty())
                                Toast.makeText(getContext(), "FAILED TO RETRIEVE RECEIPTS", Toast.LENGTH_LONG).show();
                            else {
                                DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(),
                                        DividerItemDecoration.VERTICAL);
                                mRecyclerView.addItemDecoration(divider);
                                mRecyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("BPAW", "EXCEPTION --- " + e.toString());
                    }
                }
            });
        }
        return view;
    }

    public void close() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
