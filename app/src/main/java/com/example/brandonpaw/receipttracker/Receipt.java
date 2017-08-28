package com.example.brandonpaw.receipttracker;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Brandon Paw on 6/14/2017.
 */

public class Receipt {

    // Fields to write to the firebase database
    public Long rid;
    Account owner;
    public String receipt;
    public double tip;
    public double tax;
    public double total;
    public String date;
    public String description;
    public String folders;
    public byte[] photo;

    public Receipt() {}

    public Receipt(String receipt, double tip, double tax, double total, String input_folders, String path) {
        this.receipt = receipt;
        this.tip = tip;
        this.tax = tax;
        this.total = total;
        this.folders = input_folders;
//        this.photoPath = path;
    }

    public static Receipt JSONtoReceipt(JSONObject json) throws JSONException {
        Receipt convertedRec = new Receipt();
        convertedRec.receipt = json.getString("receipt");
        convertedRec.date = json.getString("date");
        convertedRec.total = json.getDouble("total");
        convertedRec.tip = json.getDouble("tip");
        convertedRec.tax = json.getDouble("tax");
        convertedRec.rid = json.getLong("rid");
        convertedRec.owner = new Account(json.getJSONObject("owner"));
        return convertedRec;
    }

    public static JSONObject ReceiptToJSON(Receipt rec) throws JSONException {
        JSONObject json = new JSONObject();

        // Receipt data that is filled out
        json.put("receipt", rec.receipt);
        json.put("tip", rec.tip);
        json.put("tax", rec.tax);
        json.put("total", rec.total);
        json.put("description", rec.description);
        json.put("date", rec.date);

        // Account based data
//        json.put("owner", PersistentDataSingleton.persistentData.user);
        json.put("folders", rec.folders);

        return json;
    }

    @Override
    public String toString() {
        return receipt + " : " + rid;
    }
}
