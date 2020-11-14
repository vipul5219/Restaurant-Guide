package ca.mobile.restaurantguide;

import  androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_restaurants extends AppCompatActivity {
    Button addResBtn;
    EditText addResText,addResTag,addResDesc,addResAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_restaurants);
            addResBtn = findViewById(R.id.addResButton);
            addResText = findViewById(R.id.addResText);
            addResTag = findViewById(R.id.addResTag);
            addResDesc = findViewById(R.id.addResDesc);
            addResAddr = findViewById(R.id.addResAddr);
            addResBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestaurantDatabase db = new RestaurantDatabase(addResText.getText().toString(),addResDesc.getText().toString(), addResTag.getText().toString(), addResAddr.getText().toString());
                    Toast.makeText(add_restaurants.this,db.toString(),Toast.LENGTH_SHORT).show();

                }
            });
    }
}