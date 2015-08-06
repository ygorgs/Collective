package com.example.yuricesar.collective.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win 7 on 13/07/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "myDataBase";
    private static final int VERSION = 1;
    private static DataBaseHelper instance = null;

    private DataBaseHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    public static DataBaseHelper getInstance(Context context) {
        if(instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE user( "
                + "id TEXT PRIMARY KEY, "
                + "name TEXT,"
                + "email TEXT,"
                + "picture TEXT"
                +
                ")");

        db.execSQL("CREATE TABLE friendship( "
                + "id TEXT PRIMARY KEY, "
                + "FOREIGN KEY(id_user1) REFERENCES user(id), "
                + "FOREIGN KEY(id_user2) REFERENCES user(id)"
                +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE friendship");
        db.execSQL("DROP TABLE user");
        onCreate(db);

    }

    public void insertUser(UserInfo user) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user.getId());
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("picture", user.getURLPicture());

        db.insert("user", null, contentValues);

        db.close();

    }

    public void insertFriendship(UserInfo user1, UserInfo user2) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user1.getId()+user2.getId());
        contentValues.put("id_user1", user1.getId());
        contentValues.put("id_user2", user2.getId());

        db.insert("friendship", null, contentValues);

        db.close();

    }

    public boolean costainsUser(String id) {
        for(UserInfo user : selectAllUsers()) {
            if(user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public List<UserInfo> selectAllUsers() {

        List<UserInfo> users = new ArrayList<UserInfo>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);

        if(cursor.moveToFirst()) {

            do {
                UserInfo user = new UserInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));

                users.add(user);
            } while(cursor.moveToNext());

        }
        db.close();
        return users;
    }

    /*public List<UserInfo> selectAllFriends(String id_user) {

        List<UserInfo> friends = new ArrayList<UserInfo>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user", null);

        if(cursor.moveToFirst()) {

            do {
                UserInfo user = new UserInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));

                friends.add(user);
            } while(cursor.moveToNext());

        }
        db.close();
        return friends;
    } */

    public UserInfo getUser(String id) {
        UserInfo result = null;
        List<UserInfo> users = selectAllUsers();
        for (UserInfo u: users) {
            if (u.getId().equals(id)) {
                result = u;
            }
        }
        return result;
    }
}
