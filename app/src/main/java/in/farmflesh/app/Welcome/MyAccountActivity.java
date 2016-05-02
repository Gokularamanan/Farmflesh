package in.farmflesh.app.Welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import in.farmflesh.app.R;
import in.farmflesh.app.utils.SharedPreferenceFarm;

public class MyAccountActivity extends AppCompatActivity {

    private SharedPreferenceFarm sharedPreferenceFarm;
    private static final String TAG = "MyAccountActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        sharedPreferenceFarm = new SharedPreferenceFarm();
        String emailId = sharedPreferenceFarm.getStringPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_EMAIL);
        String name = sharedPreferenceFarm.getStringPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_NAME);
        Log.d(TAG, "emailId: " + emailId);
        Log.d(TAG, "name: " + name);
        TextView textViewemail = (TextView) findViewById(R.id.myaccemail);
        TextView textViewname = (TextView) findViewById(R.id.myaccname);
        textViewemail.setText(emailId);
        textViewname.setText(name);


    }
}
