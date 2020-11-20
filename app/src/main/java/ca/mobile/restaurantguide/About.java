package ca.mobile.restaurantguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {
TextView textViewName1,textViewName2,textViewName3,textViewName4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        textViewName1 = (TextView)findViewById(R.id.devarshi);
        textViewName1.setText("Devarshi Sagar:101203070");
        textViewName2 = (TextView)findViewById(R.id.saba);
        textViewName2.setText("Fahima Chowdhury:101185665");
        textViewName3 = (TextView)findViewById(R.id.fahima);
        textViewName3.setText("Saba Negatu:101223478");
        textViewName3 = (TextView)findViewById(R.id.vipul);
        textViewName3.setText("Vipulkumar Chaudhari:101206944");



    }

}