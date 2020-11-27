package ca.mobile.restaurantguide;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Share extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Button shareButton = findViewById(R.id.shareButton);

        final EditText shareCaption = findViewById(R.id.sharecaption);
        final EditText shareAddress = findViewById(R.id.shareAddress);
        final EditText shareName = findViewById(R.id.shareName);


        Intent intent = getIntent();
        final String str = intent.getStringExtra("resDetails");
        final String addr = intent.getStringExtra("resAddrs");
        final String name = intent.getStringExtra("name");
       final String rate = intent.getStringExtra("rate");

        shareCaption.setText(str);
        shareAddress.setText(addr);
        shareName.setText(name);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, name+"\n"+addr+"\n"+str+"\n"+rate);
                startActivity(Intent.createChooser(intent, "Share"));

            }
        });
    }

}