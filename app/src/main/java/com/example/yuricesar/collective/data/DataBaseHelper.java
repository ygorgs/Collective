package com.example.yuricesar.collective.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Win 7 on 13/07/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "myDataBase";
    private static final int VERSION = 2;

    public DataBaseHelper(Context context) {

        super(context, DATA_BASE, null, VERSION);
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
                + "id_user1 TEXT,"
                + "id_user2 TEXT"
                +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE friendship");
        db.execSQL("DROP TABLE user");
        onCreate(db);

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
    }

     */

}
