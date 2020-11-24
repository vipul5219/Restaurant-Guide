package ca.mobile.restaurantguide;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    List<RestaurantDatabase> restaurantList;
    SQLiteDatabase mDatabase;
    ListView listViewRestaurant;
    RestaurantAdapter adapter;
    public static final String TABLE_NAME = "restaurants";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        listViewRestaurant = findViewById(R.id.listViewRestaurants);
        restaurantList = new ArrayList<>();

        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);

        //this method will display the Restaurants in the list
        showRestaurantsFromDatabase();

    }

    private void showRestaurantsFromDatabase() {

        //we used rawQuery(sql, selectionargs) for fetching all the employees
        Cursor cursorRestaurants = mDatabase.rawQuery("SELECT * FROM TABLE_NAME", null);

        //if the cursor has some data
        if (cursorRestaurants.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the employee list
                restaurantList.add(new RestaurantDatabase(
                        cursorRestaurants.getInt(0),
                        cursorRestaurants.getString(1),
                        cursorRestaurants.getString(2),
                        cursorRestaurants.getString(3),
                        cursorRestaurants.getString(4),
                        cursorRestaurants.getDouble(5)
                ));
            } while (cursorRestaurants.moveToNext());
        }
        //closing the cursor
        cursorRestaurants.close();

        //creating the adapter object
        adapter = new RestaurantAdapter(this, R.layout.list_layout_restaurant, restaurantList,mDatabase);

        //adding the adapter to listview
        listViewRestaurant.setAdapter(adapter);
    }
}