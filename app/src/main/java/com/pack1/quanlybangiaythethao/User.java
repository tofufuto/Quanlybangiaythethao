package com.pack1.quanlybangiaythethao;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.util.BitSet;
import java.util.Date;

public class User {


    private int userId;
    private String fname;
    private String lname;
    private Date birth;
    private String role;
    private String gender;
    private Date dateAdded;
    private String numbers;
    private String gmail;
    private Bitmap avatar;

    public User(String fname, String lname, Date birth, String role, String gender,  String numbers, String gmail, Bitmap avatar) {
        this.fname = fname;
        this.lname = lname;
        this.birth = birth;
        this.role = role;
        this.gender = gender;
        this.dateAdded = new Date();
        this.numbers = numbers;
        this.gmail = gmail;
        this.avatar = avatar;
    }
    public User(String fname, String lname, Date birth, String role, String gender,  String numbers, String gmail, Bitmap avatar,int userId) {
        this.fname = fname;
        this.lname = lname;
        this.birth = birth;
        this.role = role;
        this.gender = gender;
        this.dateAdded = new Date();
        this.numbers = numbers;
        this.gmail = gmail;
        this.avatar = avatar;
        this.userId= userId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Date getBirth() {
        return birth;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
