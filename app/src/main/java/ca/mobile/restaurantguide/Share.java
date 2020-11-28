package ca.mobile.restaurantguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Share extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Button shareButton = findViewById(R.id.shareButton);

        final TextView shareDescription = findViewById(R.id.shareDescription);
        final TextView shareAddress = findViewById(R.id.shareAddress);
        final TextView shareName = findViewById(R.id.shareName);


        Intent intent = getIntent();
        final String desc = intent.getStringExtra("resDetails");
        final String addr = intent.getStringExtra("resAddrs");
        final String name = intent.getStringExtra("name");
        final String rate = intent.getStringExtra("rate");

        shareDescription.setText(desc);
        shareAddress.setText(addr);
        shareName.setText(name);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Name: "+name+"\nAddress: "+addr+"\nDescription: "+desc+"\nRatings: "+rate);
                startActivity(Intent.createChooser(intent, "Share"));


            }
        });
    }

}