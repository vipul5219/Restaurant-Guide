package ca.mobile.restaurantguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String DATABASE_NAME = "myRestaurantdatabase";
    public static final String TABLE_NAME = "restaurants";

    TextView textViewRestaurants;
    EditText editTextName, editTextAddress, editTextDescription, editTextTag,editTextRating;

    SQLiteDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRestaurants = findViewById(R.id.textViewRestaurant);
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextTag = findViewById(R.id.editTextTag);
        editTextRating = findViewById(R.id.editTextrating);

        findViewById(R.id.buttonAddRestaurant).setOnClickListener(this);
        textViewRestaurants.setOnClickListener(this);

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        //mDatabase.execSQL("DROP TABLE IF EXISTS TABLE_NAME");
        createRestaurantTable();
    }

    private void createRestaurantTable() {

        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS TABLE_NAME (\n" +
                        "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"+
                        "    name varchar(200) NOT NULL,\n" +
                        "    address varchar(200) NOT NULL,\n" +
                        "    description varchar(200) NOT NULL,\n" +
                        "    tags varchar(50) NOT NULL,\n" +
                        "    ratings Double  NOT NULL\n" +

                        ");"
        );
    }

    private boolean inputsAreCorrect(String name, String address, String description, String tags, Double rating) {
        if (name.isEmpty()|| address.isEmpty() || description.isEmpty()|| tags.isEmpty()||rating>5)
        {
            Toast.makeText(this,"Please Enter Values or rating can't be more than 5!!!!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void addRestaurant() {

        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String description = editTextDescription.getText().toString();
        String tags = editTextTag.getText().toString();
        Double ratings;
        try {
             ratings = Double.parseDouble(editTextRating.getText().toString());
        } catch (NumberFormatException e) {
            ratings = 0.0; // your default value
        }

        //validating the inputs
        if(inputsAreCorrect(name,address,description,tags,ratings)) {

            String insertSQL = "INSERT INTO TABLE_NAME \n" +
                    "(name, address, description, tags,ratings)\n" +
                    "VALUES \n" +
                    "(?, ?, ?, ?, ?);";

            //first is the sql string and second is the parameters that is to be binded with the query
            mDatabase.execSQL(insertSQL, new String[]{name, address, description, tags, String.valueOf(ratings)});

            Toast.makeText(this, "Restaurant Added Successfully", Toast.LENGTH_SHORT).show();
            editTextName.setText("");
            editTextDescription.setText("");
            editTextAddress.setText("");
            editTextTag.setText("");
            editTextRating.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddRestaurant:

                addRestaurant();

                break;
            case R.id.textViewRestaurant:

                startActivity(new Intent(this, RestaurantActivity.class));

                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_us:
                startActivity(new Intent(MainActivity.this,About.class));
             return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}