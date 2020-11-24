package ca.mobile.restaurantguide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantDatabase>
{
    Context mCtx;
    int listLayoutRes;
    List<RestaurantDatabase> restaurantList;
    SQLiteDatabase mDatabase;
    public static final String TABLE_NAME = "restaurants";

    public RestaurantAdapter(Context mCtx, int listLayoutRes, List<RestaurantDatabase> restaurantList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, restaurantList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.restaurantList = restaurantList;
        this.mDatabase = mDatabase;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting Restaurants of the specified position
        final RestaurantDatabase restaurant = restaurantList.get(position);

        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        TextView textViewAddress = view.findViewById(R.id.textViewAddress);
        TextView textViewTags = view.findViewById(R.id.textViewTags);
        TextView textViewRating =  view.findViewById(R.id.textViewRating);

        //adding data to views
        textViewName.setText("Name: "+restaurant.getName());
        textViewDescription.setText("Desc: "+restaurant.getDescription());
        textViewAddress.setText("Address: "+String.valueOf(restaurant.getAddress()));
        textViewRating.setText("Rating: "+String.valueOf(restaurant.getRating()));
        textViewTags.setText("Tags: "+restaurant.getTags());

        //future use
        Button buttonDelete = view.findViewById(R.id.buttonDeleteRestaurant);
        Button buttonEdit = view.findViewById(R.id.buttonEditRestaurant);
        Button buttonShare = view.findViewById(R.id.buttonShareRestaurant);
        Button buttonMap = view.findViewById(R.id.buttonMapRestaurant);


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM TABLE_NAME WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{restaurant.getId()});
                       reloadRestaurantFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx,EditRestaurant.class);
                mCtx.startActivity(myIntent);

            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx,Map.class);
                mCtx.startActivity(myIntent);

            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx,Share.class);
                mCtx.startActivity(myIntent);
            }
        });

        return view;
    }

    private void reloadRestaurantFromDatabase() {
        Cursor cursorRestaurants = mDatabase.rawQuery("SELECT * FROM TABLE_NAME", null);
        if (cursorRestaurants.moveToFirst()) {
            restaurantList.clear();
            do {
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
        cursorRestaurants.close();
        notifyDataSetChanged();
    }

}




