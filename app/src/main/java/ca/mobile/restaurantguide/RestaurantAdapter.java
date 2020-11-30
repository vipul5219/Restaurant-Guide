package ca.mobile.restaurantguide;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantDatabase> {
    Context mCtx;
    int listLayoutRes;
    List<RestaurantDatabase> restaurantList;
    List<RestaurantDatabase> originalRestaurantList;
    SQLiteDatabase mDatabase;

    public static final String TABLE_NAME = "restaurants";

    public RestaurantAdapter(Context mCtx, int listLayoutRes, List<RestaurantDatabase> restaurantList,List<RestaurantDatabase> originalRestaurantList ,SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, restaurantList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.restaurantList = restaurantList;
        this.mDatabase = mDatabase;
        this.originalRestaurantList = restaurantList;


    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting Restaurants of the specified position
       final RestaurantDatabase restaurant = restaurantList.get(position);

        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        TextView textViewAddress = view.findViewById(R.id.textViewAddress);
        TextView textViewTags = view.findViewById(R.id.textViewTags);
        TextView textViewRating = view.findViewById(R.id.textViewRating);

        //adding data to views
        textViewName.setText("Name: " + restaurant.getName());
        textViewDescription.setText("Desc: " + restaurant.getDescription());
        textViewAddress.setText("Address: " + restaurant.getAddress());
        textViewRating.setText("Rating: " + String.valueOf(restaurant.getRating()));
        textViewTags.setText("Tags: " + restaurant.getTags());

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
                final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

                LayoutInflater inflater = LayoutInflater.from(mCtx);
                View view = inflater.inflate(R.layout.update_restaurant, null);
                builder.setView(view);


                final EditText editName = view.findViewById(R.id.editName);
                final EditText editDesc = view.findViewById(R.id.editDesc);
                final EditText editAddress = view.findViewById(R.id.editAddress);
                final EditText editTag = view.findViewById(R.id.editTags);
                final EditText editRatings = view.findViewById(R.id.editRating);


                editName.setText(restaurant.getName());
                editAddress.setText(restaurant.getAddress());
                editDesc.setText(restaurant.getDescription());
                editTag.setText(restaurant.getTags());
                editRatings.setText(String.valueOf(restaurant.getRating()));

                final AlertDialog dialog = builder.create();
                dialog.show();


                view.findViewById(R.id.buttonUpdateRestaurant).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editName.getText().toString().trim();
                        String address = editAddress.getText().toString().trim();
                        String description = editDesc.getText().toString().trim();
                        String tags = editTag.getText().toString().trim();
                        Double rating;
                        try {
                            rating = Double.parseDouble(editRatings.getText().toString());
                        } catch (NumberFormatException e) {
                            rating = 0.0; // your default value
                        }


                        if(inputsAreCorrect(name,address,description,tags,rating)) {
                            String sql = "UPDATE TABLE_NAME \n" +
                                    "SET name = ?, \n" +
                                    "description = ?, \n" +
                                    "address = ?, \n" +
                                    "tags = ?, \n" +
                                    "ratings = ? \n" +
                                    "WHERE id = ?;\n";

                            // mDatabase.update(TABLE_NAME,values,"id=?",new String[]{String.valueOf(restaurant.getId())});
                            mDatabase.execSQL(sql, new String[]{name, description, address, tags, String.valueOf(rating), String.valueOf(restaurant.getId())});
                            Toast.makeText(mCtx, "Restaurant Updated", Toast.LENGTH_SHORT).show();
                            reloadRestaurantFromDatabase();
                            dialog.dismiss();
                        }
                    }

                });
            }
        });


/*
        buttonEdit.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx,EditRestaurant.class);
                myIntent.putExtra("Id",String.valueOf(restaurant.getId()));
                myIntent.putExtra("Name",String.valueOf(restaurant.getName()));
                myIntent.putExtra("Address",String.valueOf(restaurant.getAddress()));
                myIntent.putExtra("Desc",String.valueOf(restaurant.getDescription()));
                myIntent.putExtra("Tag",String.valueOf(restaurant.getTags()));
                myIntent.putExtra("Rating",String.valueOf(restaurant.getRating()));


                mCtx.startActivity(myIntent);

            }
        });
*/


        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx, Map.class);
                mCtx.startActivity(myIntent);

            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mCtx, Share.class);
                String Desc,addrs,name;
                String rate;
                Desc  = restaurant.getDescription();
                addrs = restaurant.getAddress();
                name = restaurant.getName();
                rate = String.valueOf(restaurant.getRating());
                myIntent.putExtra("resDetails",Desc);
                myIntent.putExtra("name",name);
                myIntent.putExtra("rate",rate);
                myIntent.putExtra("resAddrs",addrs);
                mCtx.startActivity(myIntent);
            }
        });

        return view;
    }

    private boolean inputsAreCorrect(String name, String address, String description, String tags, Double rating) {
        if (name.isEmpty() || address.isEmpty() || description.isEmpty() || tags.isEmpty()) {
            Toast.makeText(mCtx, "Please Enter Values!!!!", Toast.LENGTH_LONG).show();
            return false;
        }
         else if( rating > 5)
         {
             Toast.makeText(mCtx, "Rating can't be more than 5!!!!", Toast.LENGTH_LONG).show();
             return false;
         }
          else
              {
            return true;
        }
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

    public void searchRest(String query,List<RestaurantDatabase> list){

        if (!query.isEmpty()){
            List<RestaurantDatabase> filtered = new ArrayList<>();
            for (int i =0; i<list.size();i++)
            {
                if (list.get(i).name.contains(query) || (list.get(i).address.contains(query) )){
                    filtered.add(list.get(i));
            }

            }
            if (filtered!=null && filtered.size()>0) {
                restaurantList.clear();
                restaurantList.addAll(filtered);
                notifyDataSetChanged();
            }
            }
    }

    public void showOriginal(List<RestaurantDatabase> filtered){
        restaurantList.clear();
        restaurantList.addAll(filtered);
        notifyDataSetChanged();
    }
}




