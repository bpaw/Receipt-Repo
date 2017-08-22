package com.example.brandonpaw.receipttracker;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brandon Paw on 8/13/2017.
 */

public class UtilREST {

    Context mContext;
    final List<Receipt> receipts = new ArrayList<>();
    final List<String> folders = new ArrayList<>();

    public UtilREST(Activity context) {
        this.mContext = context;
    }

    public void getAllAccounts() {
        Log.e("BPAW","CALLING GET ALL ACCOUNTS");
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.9:8080/ReceiptRepoREST/rest/accounts/";
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Log.e("BPAW", error.toString());
                Toast.makeText(mContext, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(strReq);
    }

    public void getAllReceipts() {
        Log.e("BPAW", "Calling get all receipts");
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/receipts/account/1";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response is " + response);
                Log.e("BPAW", "DID NOT ERROR");

                try {
                    JSONArray jsons = response.getJSONArray("receipts");
                    if (jsons != null) {
                        Log.e("BPAW", "JSON ARRAY LENGTH : " + jsons.length());
                        for (int i = 0; i < jsons.length(); i++) {
                            Log.e("BPAW", Receipt.JSONtoReceipt(jsons.getJSONObject(i)).toString());
                            receipts.add(Receipt.JSONtoReceipt(jsons.getJSONObject(i)));
                        }
                        Log.e("BPAW", "NOW LENGTH IS --- " + receipts.size());
                    }
                } catch (JSONException e) {
                    Log.e("BPAW", "EXCEPTION --- " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BPAW", "ERROR " + error.toString());
            }
        });
//        receiptsquest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void getFolders() {
        Log.e("BPAW", "Calling GET FOLDERS");
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/accounts/folders/1";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("BPAW", "Folders are : "+response.toString());
                int len = response.length();
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        try {
                            String folder = response.get(i).toString();
                            Log.e("BPAW", "The folder is : " + folder);
                            folders.add(folder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BPAW", "ERROR GETTING FOLDERS --- " + error.toString());
            }
        });
        queue.add(request);
    }

    public void createReceipt(Long accountId, Receipt newRec) throws JSONException {
        Log.e("BPAW", "Calling createReceipt in UtilREST");
        final Long id = accountId;
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/receipts/" + accountId;
        JSONObject rec_json = new JSONObject();
        rec_json.put("receipt", newRec.receipt);
        rec_json.put("total", newRec.total);
        JsonObjectRequest request = new JsonObjectRequest(url, rec_json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("BPAW", "\tResponse came back WITHOUT errors");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BPAW", "\tResponse came back WITH errors");
            }
        });
        queue.add(request);
    }
}
