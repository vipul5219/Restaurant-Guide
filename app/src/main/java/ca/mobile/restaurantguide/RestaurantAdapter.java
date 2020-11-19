package ca.mobile.restaurantguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantDatabase> implements View.OnClickListener
{

    Context mCtx;
    int listLayoutRes;
    List<RestaurantDatabase> restaurantList;
    SQLiteDatabase mDatabase;


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
        RestaurantDatabase restaurant = restaurantList.get(position);


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

        return view;
    }
    @Override
    public void onClick(View v) {

    }

}
