package com.example.brandonpaw.receipttracker;

import java.util.ArrayList;

/**
 * Created by Brandon Paw on 6/12/2017.
 */

public class User {

    public String userName;
    public ArrayList<String> folders;

    public User() {

    }

    public User(String name) {

        userName = name;
    }

    public User(String name, ArrayList<String> fold) {

        userName = name;
        folders = fold;
    }

    public boolean addFolder(String newFolder) {

        if (!folders.contains(newFolder)) {
            folders.add(newFolder);
            return true;
        }
        return false;
    }
}
