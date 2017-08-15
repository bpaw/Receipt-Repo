package com.example.brandonpaw.receipttracker;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Brandon Paw on 8/13/2017.
 */

public class UtilREST {

    Context mContext;

    public UtilREST(Activity context) {
        this.mContext = context;
    }

    public void getAllAccounts() {
        Log.e("HERE HERE HERE","CALLING GET ALL ACCOUNTS");
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.1:8080/ReceiptRepoREST/rest/accounts/";
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(mContext, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(strReq);
    }
}
