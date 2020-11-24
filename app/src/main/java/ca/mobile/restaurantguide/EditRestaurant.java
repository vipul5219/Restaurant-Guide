package ca.mobile.restaurantguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class EditRestaurant extends AppCompatActivity {

    Context mCtx;
    List<RestaurantDatabase> restaurantList;
    RestaurantDatabase restaurantDatabase;
    String Name,Address,Desc,Tag,Rating;
    EditText editName,editAddress,editDesc,editTags,editRating;
    Button Update;
    public static final String TABLE_NAME = "restaurants";
    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_restaurant);


        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editaddress);
        editDesc = findViewById(R.id.editDesc);
        editTags = findViewById(R.id.editTags);
        editRating = findViewById(R.id.editRating);
        Update = findViewById(R.id.editButton);

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editName.getText().toString().trim();
                String address = editAddress.getText().toString().trim();
                String description = editDesc.getText().toString().trim();
                String tags = editTags.getText().toString().trim();
                Double rating;
                try {
                    rating = Double.parseDouble(editRating.getText().toString());
                } catch (NumberFormatException e) {
                    rating = 0.0; // your default value
                }

                if(inputsAreCorrect(name,address,description,tags,rating))
                {
                    String sql = "UPDATE restaurants \n" +
                            "SET name = ?, \n" +
                            "description = ?, \n" +
                            "address = ?, \n" +
                            "tags = ?, \n" +
                            "rating = ? \n" +
                            "WHERE id = ?;\n";

                    mDatabase.execSQL(sql, new String[]{name, description, address,tags,String.valueOf(rating), String.valueOf(restaurantDatabase.getId())});
                    Toast.makeText(mCtx, "Restaurant Updated", Toast.LENGTH_SHORT).show();
                   // reloadRestaurantFromDatabase();
                }
                else {
                    Toast.makeText(mCtx,"Something Wrong",Toast.LENGTH_LONG).show();
                }

            }
        });

        getIntentData();
    }

    void getIntentData()
    {
        if(getIntent().hasExtra("Name")&&getIntent().hasExtra("Address")
                &&getIntent().hasExtra("Desc")&&getIntent().hasExtra("Tag")&&getIntent().hasExtra("Rating"))
        {
            Name = getIntent().getStringExtra("Name");
            Address = getIntent().getStringExtra("Address");
            Desc = getIntent().getStringExtra("Desc");
            Tag = getIntent().getStringExtra("Tag");
            Rating = getIntent().getStringExtra("Rating");

            editName.setText(Name);
            editAddress.setText(Address);
            editDesc.setText(Desc);
            editTags.setText(Tag);
            editRating.setText(String.valueOf(Rating));

        }
        else
        {
            Toast.makeText(this,"No data",Toast.LENGTH_LONG).show();
        }
    }

    private boolean inputsAreCorrect(String name, String address, String description, String tags, Double rating) {
        if (name.isEmpty()|| address.isEmpty() || description.isEmpty()|| tags.isEmpty()||rating.isNaN()||rating>5)
        {
            Toast.makeText(this,"Please Enter Values or rating can't be more than 5!!!!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
       // notifyDataSetChanged();
    }
}
