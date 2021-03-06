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
    public static final String POST_TABLE = "post_table";
    public static final String REPLY_TABLE = "reply_table";

    public static final String USER_COL_1 = "USERNAME";
    public static final String USER_COL_2 = "FIRST_NAME";
    public static final String USER_COL_3 = "LAST_NAME";
    public static final String USER_COL_4 = "PASSWORD";
    public static final String USER_COL_5 = "BALANCE";
    public static final String USER_COL_6 = "SECURITY_QUESTION";
    public static final String USER_COL_7 = "SECURITY_ANSWER";

    public static final String ORDERS_COL_1 = "ORDERS_ID";
    public static final String ORDERS_COL_2 = "COIN_SYMBOL";
    public static final String ORDERS_COL_3 = "USERNAME";
    public static final String ORDERS_COL_4 = "TYPE";
    public static final String ORDERS_COL_5 = "AMOUNT";


    public static final String WATCHLIST_COL_1 = "WATCHLIST_ID";
    public static final String WATCHLIST_COL_2 = "COIN_SYMBOL";
    public static final String WATCHLIST_COL_3 = "USERNAME";
    public static final String WATCHLIST_COL_4 = "COIN_NAME";

    public static final String PORTFOLIO_COL_1 = "PORTFOLIO_ID";
    public static final String PORTFOLIO_COL_2 = "COIN_SYMBOL";
    public static final String PORTFOLIO_COL_3 = "USERNAME";
    public static final String PORTFOLIO_COL_4 = "AMOUNT";
    public static final String PORTFOLIO_COL_5 = "INITIAL_INVESTMENT";

    public static final String POST_COL_1 = "POST_ID";
    public static final String POST_COL_2 = "USERNAME";
    public static final String POST_COL_3 = "POST_MESSAGE";
    public static final String POST_COL_4 = "TIMESTAMP";

    public static final String REPLY_COL_1 = "REPLY_ID";
    public static final String REPLY_COL_2 = "POST_ID";
    public static final String REPLY_COL_3 = "USERNAME";
    public static final String REPLY_COL_4 = "REPLY_MESSAGE";
    public static final String REPLY_COL_5 = "TIMESTAMP";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USER_TABLE + " (USERNAME text primary key, FIRST_NAME text, LAST_NAME text, PASSWORD text, BALANCE DOUBLE, SECURITY_QUESTION text, SECURITY_ANSWER text)");
        sqLiteDatabase.execSQL("CREATE TABLE " + ORDERS_TABLE + " (ORDERS_ID integer primary key autoincrement, COIN_SYMBOL text, USERNAME text, TYPE text, AMOUNT DOUBLE," +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + WATCHLIST_TABLE + " (WATCHLIST_ID integer primary key autoincrement, COIN_SYMBOL text, USERNAME text, COIN_NAME text, " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + PORTFOLIO_TABLE + " (PORTFOLIO_ID integer primary key autoincrement, COIN_SYMBOL text, USERNAME text, AMOUNT DOUBLE, INITIAL_INVESTMENT DOUBLE, " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + POST_TABLE + " (POST_ID integer primary key autoincrement, USERNAME text, POST_MESSAGE text, timestamp DATETIME DEFAULT (strftime('%Y-%m-%dT%H:%M', 'now')), " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username))");
        sqLiteDatabase.execSQL("CREATE TABLE " + REPLY_TABLE + " (REPLY_ID integer primary key autoincrement, POST_ID integer, USERNAME text, REPLY_MESSAGE text, timestamp DATETIME DEFAULT (strftime('%Y-%m-%dT%H:%M', 'now')) , " +
                " FOREIGN KEY (USERNAME) REFERENCES user_table (username), FOREIGN KEY (POST_ID) REFERENCES post_table (post_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WATCHLIST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PORTFOLIO_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + REPLY_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String username,String first_name, String last_name, String password, String security_question, String security_answer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_1,username);
        cv.put(USER_COL_2,first_name);
        cv.put(USER_COL_3,last_name);
        cv.put(USER_COL_4,password);
        cv.put(USER_COL_5,0.0);
        cv.put(USER_COL_6,security_question);
        cv.put(USER_COL_7,security_answer);

        long result = db.insert(USER_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertOrder(String coin_symbol, String username, String type, double amount){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ORDERS_COL_2,coin_symbol);
        cv.put(ORDERS_COL_3,username);
        cv.put(ORDERS_COL_4,type);
        cv.put(ORDERS_COL_5,amount);

        long result = db.insert(ORDERS_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertWatchlist(String coin_symbol, String username,String coin_name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(WATCHLIST_COL_2,coin_symbol);
        cv.put(WATCHLIST_COL_3,username);
        cv.put(WATCHLIST_COL_4,coin_name);

        long result = db.insert(WATCHLIST_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertPortfolio(String coin_symbol, String username, double amount, double initial_investment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PORTFOLIO_COL_2,coin_symbol);
        cv.put(PORTFOLIO_COL_3,username);
        cv.put(PORTFOLIO_COL_4,amount);
        cv.put(PORTFOLIO_COL_5,initial_investment);

        long result = db.insert(PORTFOLIO_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertPost(String username,String post_message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(POST_COL_2,username);
        cv.put(POST_COL_3,post_message);

        long result = db.insert(POST_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }

    public boolean insertReply(int post_id, String username, String reply_message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(REPLY_COL_2,post_id);
        cv.put(REPLY_COL_3,username);
        cv.put(REPLY_COL_4,reply_message);

        long result = db.insert(REPLY_TABLE, null, cv);

        if(result == -1){
            return false;
        }
        else return true;
    }


    public Cursor getUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query(USER_TABLE, new String[]{USER_COL_1,USER_COL_2, USER_COL_3, USER_COL_4, USER_COL_5, USER_COL_6, USER_COL_7}, "username = ?", new String[] {username}, null, null, null);

        return res;
    }

    public Cursor getOrder(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + ORDERS_TABLE + " WHERE USERNAME = \"" + username + "\"", null);

        return res;
    }

    public Cursor getWatchlist(String username, String coin_symbol){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + WATCHLIST_TABLE + " WHERE USERNAME = \"" + username + "\" AND COIN_SYMBOL = \"" + coin_symbol + "\"", null);

        return res;
    }

    public Cursor getWatchlistByUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + WATCHLIST_TABLE + " WHERE USERNAME = \"" + username + "\"", null);

        return res;
    }

    public Cursor getPortfolio(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + PORTFOLIO_TABLE + " WHERE USERNAME = \"" + username + "\"", null);

        return res;
    }

    public Cursor getPostByID(int post_id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + POST_TABLE + " WHERE POST_ID = " + post_id, null);

        return res;
    }

    public Cursor getAllPosts(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + POST_TABLE, null);

        return res;
    }

    public Cursor getRepliesByPost(int post_id){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + REPLY_TABLE + " WHERE POST_ID = " + post_id, null);

        return res;
    }

    public boolean updatePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_4,password);
        db.update(USER_TABLE,cv,"USERNAME = ? ", new String[] {username});

        return true;
    }

    public boolean updateBalance(String username, double balance){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(USER_COL_5,balance);
        int result = db.update(USER_TABLE,cv,"USERNAME = ? ", new String[] {username});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean updatePortfolio(String username, String coin_symbol, double amount, double initial_investment){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PORTFOLIO_COL_4,amount);
        cv.put(PORTFOLIO_COL_5,initial_investment);
        int result = db.update(PORTFOLIO_TABLE,cv,"USERNAME = ? AND COIN_SYMBOL = ?", new String[] {username,coin_symbol});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean updatePost(int post_id, String post_message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(POST_COL_3,post_message);
        db.update(USER_TABLE,cv,"POST_ID = ? ", new String[] {String.valueOf(post_id)});

        return true;
    }

    public boolean updateReply(String reply_id, String reply_message){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(REPLY_COL_4,reply_message);
        db.update(USER_TABLE,cv,"POST_ID = ? ", new String[] {String.valueOf(reply_id)});

        return true;
    }


    public Cursor getPortfolioUsernameCoin(String username, String coinSymbol){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + PORTFOLIO_TABLE + " WHERE USERNAME = \"" + username + "\" AND COIN_SYMBOL = \"" + coinSymbol + "\"", null);

        return res;
    }

    public boolean deleteWatchlist(String username, String coin_symbol){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(WATCHLIST_TABLE, "USERNAME = ? AND COIN_SYMBOL = ?", new String[] {username,coin_symbol});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean deletePortfolio(String username, String coin_symbol){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(PORTFOLIO_TABLE, "USERNAME = ? AND COIN_SYMBOL = ?", new String[] {username,coin_symbol});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean deletePost(int post_id){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(POST_TABLE, "POST_ID = ?", new String[] {String.valueOf(post_id)});

        if (result == -1){
            return false;
        }
        else return true;
    }

    public boolean deleteReply(int reply_id){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(REPLY_TABLE, "REPLY_ID = ?", new String[] {String.valueOf(reply_id)});

        if (result == -1){
            return false;
        }
        else return true;
    }
}
