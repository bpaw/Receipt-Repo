package com.example.brandonpaw.receipttracker;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Base64;
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
import com.google.android.gms.tasks.TaskCompletionSource;

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

    /**
     *  This is where the GET requests are located
     */

    public Task<JSONObject> getAccount(final String username) {
        Log.e("BPAW", "CALLING GET ACCOUNT with " + username);
        final TaskCompletionSource<JSONObject> source = new TaskCompletionSource<>();
        final Account[] account = new Account[1];
        /* TODO : May need to urlencode spaces or restrict usernames to not have spaces in them */
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.9:8080/ReceiptRepoREST/rest/accounts";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("BPAW", "The JSONObject response is : " + response.toString());
                source.setResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                source.setException(error);
//                Log.e("BPAW util 62", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };
        queue.add(request);
        return source.getTask();
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

    public Task<JSONObject> getAllReceipts(Long rid) {
        Log.e("BPAW", "Calling get all receipts");
        final TaskCompletionSource<JSONObject> source = new TaskCompletionSource<>();
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/receipts/account/" + rid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response is " + response);
                Log.e("BPAW", "DID NOT ERROR");
                source.setResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BPAW", "ERROR " + error.toString());
            }
        });
        queue.add(request);
        return source.getTask();
    }

    public Task<JSONArray> getFolders(Long rid) {
        Log.e("BPAW", "Calling GET FOLDERS");
        final TaskCompletionSource source = new TaskCompletionSource();
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/accounts/folders/" + rid;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("BPAW", "Folders are : "+response.toString());
                source.setResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                source.setException(error);
                Log.e("BPAW", "ERROR GETTING FOLDERS --- " + error.toString());
            }
        });
        queue.add(request);
        return source.getTask();
    }

    /**
     *  This is where the POST requests are located
     */

    public void createReceipt(Long accountId, Receipt newRec) throws JSONException {
        Log.e("BPAW", "Calling createReceipt in UtilREST");
        final Long id = accountId;
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        String url = "http://192.168.0.09:8080/ReceiptRepoREST/rest/receipts/" + accountId;
        JSONObject rec_json = new JSONObject();
        rec_json.put("receipt", newRec.receipt);
        rec_json.put("total", newRec.total);
        rec_json.put("photo_bytes", Base64.encode(newRec.photo, Base64.DEFAULT));
        JsonObjectRequest request = new JsonObjectRequest(url, rec_json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("BPAW", "\tResponse came back WITHOUT errors");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("BPAW", "\tResponse came back WITH errors : " + error.getMessage());
            }
        });
        queue.add(request);
    }
}
