package com.example.kantorwalut;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.sqlite";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userDatabaseSQL = "" +
                "create table user ( " +
                "   id integer primary key autoincrement," +
                "   username varchar(32)," +
                "   password varchar(256)," +
                "   role varchar(16) default 'user'" +
                ")";
        db.execSQL(userDatabaseSQL);

        String currencyDatabaseSQL = "" +
                "create table currency ( " +
                "   id integer primary key autoincrement," +
                "   name varchar(32)," +
                "   from_value varchar(64)," +
                "   to_value varchar(64)" +
                ")";
        db.execSQL(currencyDatabaseSQL);

        String passwordEncoded = PasswordUtils.encode("admin");

        ContentValues cv = new ContentValues();
        cv.put("username", "admin");
        cv.put("password", passwordEncoded);
        cv.put("role", "admin");
        db.insert("user", null, cv);

        insertCurrency(db, new Currency(0, "złoty polski (PLN)", new BigDecimal("1.0"), new BigDecimal("1.0")));
        insertCurrency(db, new Currency(0, "euro (EUR)", new BigDecimal("4.3"), new BigDecimal("4.5")));
        insertCurrency(db, new Currency(0, "dolar amerykański (USD)", new BigDecimal("3.8"), new BigDecimal("3.95")));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user;");
        db.execSQL("drop table if exists currency;");
        onCreate(db);
    }

    // user

    public boolean insertUser(String username, String password, String role) {
        String passwordEncoded = PasswordUtils.encode(password);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", passwordEncoded);
        cv.put("role", role);
        long result = db.insert("user", null, cv);
        return result != -1;
    }

    public boolean insertUser(String username, String password) {
        return insertUser(username, password, "user");
    }

    public boolean login(String username, String password) {
        String passwordEncoded = PasswordUtils.encode(password);

        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id from user " +
                    "where username like '" + username + "' and password like '" + passwordEncoded + "'", null);
            return cur.getCount() == 1;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public String getRole(String username) {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select role from user where username like '" + username + "'", null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                return cur.getString(0);
            }
            return null;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public Integer deleteUser(String username) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("user", "username = ?", new String[]{username});
    }

    // currency

    public boolean insertCurrency(Currency currency) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", currency.getName());
        cv.put("from_value", currency.getFromValue().toString());
        cv.put("to_value", currency.getToValue().toString());
        long result = db.insert("currency", null, cv);
        return result != -1;
    }

    public boolean insertCurrency(SQLiteDatabase db, Currency currency) {
        ContentValues cv = new ContentValues();
        cv.put("name", currency.getName());
        cv.put("from_value", currency.getFromValue().toString());
        cv.put("to_value", currency.getToValue().toString());
        long result = db.insert("currency", null, cv);
        return result != -1;
    }

    public Currency getCurrency(String name) {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id, name, from_value, to_value from currency where name like '" + name + "'", null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                return new Currency(cur.getInt(0), cur.getString(1), new BigDecimal(cur.getString(2)), new BigDecimal(cur.getString(3)));
            }
            return null;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public List<User> getAllUsers() {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id, username, role from user", null);

            ArrayList<User> users = new ArrayList<>();

            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    users.add(new User(cur.getInt(0), cur.getString(1), "<secret>", cur.getString(2)));
                    cur.moveToNext();
                }
            }
            return users;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public void makeAdmin(int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("role", "admin");

        db.update("user", cv, "id = ?", new String[]{String.valueOf(id)});
    }

    public List<Currency> getAllCurrencies() {
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();

        try {
            cur = db.rawQuery("select id, name, from_value, to_value from currency", null);

            ArrayList<Currency> currencies = new ArrayList<>();

            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    currencies.add(new Currency(cur.getInt(0), cur.getString(1),
                            new BigDecimal(cur.getString(2)), new BigDecimal(cur.getString(3))));
                    cur.moveToNext();
                }
            }
            return currencies;
        } finally {
            Objects.requireNonNull(cur).close();
        }
    }

    public Integer deleteCurrency(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("currency", "id = ?", new String[]{String.valueOf(id)});
    }
}
