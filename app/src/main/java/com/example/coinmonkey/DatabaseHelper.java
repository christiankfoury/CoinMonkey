package com.example.coinmonkey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "coinMonkey.db";

    public static final String USER_TABLE = "user_table";
    public static final String ORDERS_TABLE = "orders_table";
    public static final String WATCHLIST_TABLE = "watchlist_table";
    public static final String PORTFOLIO_TABLE = "portfolio_table";

    public static final String USER_COL_1 = "USERNAME";
    public static final String USER_COL_2 = "PASSWORD";
    public static final String USER_COL_3 = "BALANCE";

    public static final String ORDERS_COL_1 = "ORDERS_ID";
    public static final String ORDERS_COL_2 = "COIN_SYMBOL";
    public static final String ORDERS_COL_3 = "USERNAME";
    public static final String ORDERS_COL_4 = "TYPE";
    public static final String ORDERS_COL_5 = "AMOUNT";
    public static final String ORDERS_COL_6 = "VALUE";

    public static final String WATCHLIST_COL_1 = "WATCHLIST_ID";
    public static final String WATCHLIST_COL_2 = "COIN_SYMBOL";
    public static final String WATCHLIST_COL_3 = "USERNAME";

    public static final String PORTFOLIO_COL_1 = "PORTFOLIO_ID";
    public static final String PORTFOLIO_COL_2 = "COIN_SYMBOL";
    public static final String PORTFOLIO_COL_3 = "USERNAME";
    public static final String PORTFOLIO_COL_4 = "AMOUNT";
    public static final String PORTFOLIO_COL_5 = "VALUE";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE + " (USERNAME text primary key, PASSWORD text, BALANCE long)");
        sqLiteDatabase.execSQL("CREATE TABLE " + ORDERS_TABLE + " (ORDERS_ID integer primary key autoincrement, COIN_SYMBOL text, USERNAME text, TYPE text, AMOUNT decimal(15,2), VALUE decimal(15,2)," +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + WATCHLIST_TABLE + " (WATCHLIST_ID integer primary key autoincrement, COIN_SYMBOL text, USERNAME text, " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + PORTFOLIO_TABLE + " (PORTFOLIO_ID primary key autoincrement, COIN_SYMBOL text, USERNAME text, AMOUNT decimal(15,2), VALUE decimal(15,2), " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WATCHLIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PORTFOLIO_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_1,username);
        cv.put(USER_COL_2,password);
        cv.put(USER_COL_3,0.0);

        long result = db.insert(USER_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertOrder(String coin_symbol, String username, String type, double amount, double value){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ORDERS_COL_2,coin_symbol);
        cv.put(ORDERS_COL_3,username);
        cv.put(ORDERS_COL_4,type);
        cv.put(ORDERS_COL_5,amount);
        cv.put(ORDERS_COL_6,value);

        long result = db.insert(ORDERS_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertWatchlist(String coin_symbol, String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(WATCHLIST_COL_2,coin_symbol);
        cv.put(WATCHLIST_COL_3,username);

        long result = db.insert(ORDERS_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertPortfolio(String coin_symbol, String username, double amount, double value){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PORTFOLIO_COL_2,coin_symbol);
        cv.put(PORTFOLIO_COL_3,username);
        cv.put(PORTFOLIO_COL_4,amount);
        cv.put(PORTFOLIO_COL_5,value);

        long result = db.insert(ORDERS_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public Cursor getUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE USERNAME = " + username , null);

        return res;
    }

    public Cursor getOrder(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + ORDERS_TABLE + " WHERE USERNAME = " + username, null);

        return res;
    }

    public Cursor getWatchlist(String username, String coin_symbol){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + WATCHLIST_TABLE + " WHERE USERNAME = " + username + "AND COIN_SYMBOL = " + coin_symbol, null);

        return res;
    }

    public Cursor getPortfolio(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + PORTFOLIO_TABLE + " WHERE USERNAME = " + username, null);

        return res;
    }

    public boolean updatePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_2,password);
        db.update(USER_TABLE,cv,"USERNAME = ? ", new String[] {username});

        return true;
    }

    public boolean updateBalance(String username, double balance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_3,balance);
        int result = db.update(USER_TABLE,cv,"USERNAME = ? ", new String[] {username});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean updatePortfolio(String username, String coin_symbol, double amount, double value ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PORTFOLIO_COL_4,amount);
        cv.put(PORTFOLIO_COL_5,value);
        int result = db.update(PORTFOLIO_TABLE,cv,"USERNAME = ? AND COIN_SYMBOL = ?", new String[] {username,coin_symbol});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean deleteWatchlist(String username, String coin_symbol){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(WATCHLIST_TABLE, "USERNAME = ? AND COIN_SYMBOL = ?", new String[] {username,coin_symbol});

        if (result == -1){
            return false;
        }
        else return true;
    }
}
