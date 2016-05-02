package in.farmflesh.app.Welcome;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.farmflesh.app.R;
import in.farmflesh.app.utils.PermissionsUtils;
import in.farmflesh.app.utils.SharedPreferenceFarm;

public class WelcomeActivity extends FragmentActivity {

    private static final String RESULT = "result";
    private SharedPreferenceFarm sharedPreference;
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreference = new SharedPreferenceFarm();
        setContentView(R.layout.activity_welcome1);
        Log.d(TAG, "onCreate");


        Button next = (Button) findViewById(R.id.next_privacy);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreference.saveBooleanPrefByKey(getApplicationContext(),
                        SharedPreferenceFarm.PREFS_KEY_IS_PRIVACY_ACCEPTED, true);
                Intent resultData = new Intent();
                resultData.putExtra(RESULT, true);
                setResult(RESULT_OK, resultData);
                finish();
            }
        });
    }


}
