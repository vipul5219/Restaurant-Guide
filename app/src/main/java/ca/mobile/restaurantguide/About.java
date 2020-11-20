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
        textViewName1.setText("Devarshi:101203070");
        textViewName2 = (TextView)findViewById(R.id.saba);
        textViewName2.setText("Saba:101223478");
        textViewName3 = (TextView)findViewById(R.id.fahima);
        textViewName3.setText("Fahima:101185665");
        textViewName3 = (TextView)findViewById(R.id.vipul);
        textViewName3.setText("Vipul:101206944");



    }

}