package com.example.brandonpaw.receipttracker;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Brandon Paw on 6/14/2017.
 */

public class Receipt {

    // Fields to write to the firebase database
    public String receipt;
    public int tip;
    public int tax;
    public int total;
    public ArrayList<String> folders;
    public String photoPath;

    public Receipt() {}

    public Receipt(String receipt, int tip, int tax, int total, ArrayList<String> Xfolders, String path) {
        this.receipt = receipt;
        this.tip = tip;
        this.tax = tax;
        this.total = total;
        this.folders = new ArrayList<>();
        for (String folder : Xfolders) {

            folders.add(folder);
        }
        this.photoPath = path;
    }
}
