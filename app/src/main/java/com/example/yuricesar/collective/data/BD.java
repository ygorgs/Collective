package com.example.yuricesar.collective.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diego on 09/08/2015.
 */
public class BD {

    private SQLiteDatabase bd;

    public BD(Context context){

        DataBaseHelper auxBD = new DataBaseHelper(context);
        bd = auxBD.getWritableDatabase();
    }

    public void insertUser(UserInfo user) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user.getId());
        contentValues.put("name", user.getName());
        contentValues.put("email", user.getEmail());
        contentValues.put("picture", user.getURLPicture());

        bd.insert("user", null, contentValues);

    }

    public List<UserInfo> selectAllUsers() {

        List<UserInfo> users = new ArrayList<UserInfo>();

        String[] colunas = new String[]{"id", "name", "email", "picture"};

        Cursor cursor = bd.query("user",colunas,null,null,null,null,null );

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                UserInfo user = new UserInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3));
                users.add(user);
            } while(cursor.moveToNext());

        }

        return users;
    }

    public int quantidadeDeUsuarios(){

        int quantidade = 0;

        String[] colunas = new String[]{"id", "name", "email", "picture"};

        Cursor cursor = bd.query("user",colunas,null,null,null,null,null );

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                quantidade++;
            } while(cursor.moveToNext());

        }

        return quantidade;
    }

    public boolean containsUser(String id) {
        for(UserInfo user : selectAllUsers()) {
            if(user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

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

    public UserInfo getUserName(String id) {
        UserInfo result = null;
        List<UserInfo> users = selectAllUsers();
        for (UserInfo u: users) {
            if (u.getName().equals(id)) {
                result = u;
            }
        }
        return result;
    }

    public List<UserInfo> selectAllFriends(UserInfo userInfo) {

        List<UserInfo> users = new ArrayList<UserInfo>();

        Cursor cursor = bd.rawQuery("SELECT id_user2 FROM friendship WHERE id_user1 = ?", new String[]{userInfo.getId()});

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                users.add(getUser(cursor.getString(0)));
            } while(cursor.moveToNext());
        }
        return users;
    }

    public void inserirAmigo(UserInfo logado, UserInfo amigo){

        ContentValues contentValues = new ContentValues();

        contentValues.put("id_user1", logado.getId());
        contentValues.put("id_user2", amigo.getId());

        bd.insert("friendship",null,contentValues);
    }
}
