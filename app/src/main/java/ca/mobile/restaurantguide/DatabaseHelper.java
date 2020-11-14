package ca.mobile.restaurantguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String RESTAURANT_TABLE = "RESTAURANT_TABLE";
    public static final String ID = "ID";
    public static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    public static final String DESCRIPTION1 = "DESCRIPTION";
    public static final String DESCRIPTION = DESCRIPTION1;
    public static final String TAGS = "TAGS";
    public static final String ADDRESS = "ADDRESS";
    public static final String RATING = "RATING";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "ResDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStatement = "CREATE TABLE " + RESTAURANT_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + RESTAURANT_NAME + "TEXT," + DESCRIPTION + "TEXT, " + TAGS + "TEXT,  " + ADDRESS + " TEXT," + RATING + "DECIMAL)";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(RestaurantDatabase rdb)
    {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long insert = db.insert(RESTAURANT_TABLE,null,cv);
        if(insert==-1){
            return false;
        }
        else{
            return true;
        }
    }
}
