package in.farmflesh.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import in.farmflesh.app.utils.AsyncResponse;
import in.farmflesh.app.utils.RegistrationRecord;
import in.farmflesh.app.utils.SharedPreferenceFarm;

public class WelcomeActivity extends AppCompatActivity implements AsyncResponse {
    private SharedPreferenceFarm sharedPreferenceFarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferenceFarm = new SharedPreferenceFarm();

        Button button = (Button) findViewById(R.id.signIn_welcome);
        final EndpointsAsyncTask asyncTask = new EndpointsAsyncTask(WelcomeActivity.this);
        asyncTask.delegate = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGmailReg = sharedPreferenceFarm.getBooleanPrefByKey
                        (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_IS_GMAIL_REG);
                if (asyncTask.getStatus() == AsyncTask.Status.PENDING || !isGmailReg) {
                    //execute the async task
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("email", "gok@gmail.com");
                        jo.put("name", "Doe");
                        jo.put("phone", "32432");
                        asyncTask.execute(new Pair<Context, String>(WelcomeActivity.this, jo.toString()));
                    }catch(JSONException ex){
                        Log.d("Gok", ex.toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Already GCM registered", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        if (output.equals("Hi, Manfred")) {
            Log.d("Gok", "GCM reg success");
            sharedPreferenceFarm.saveBooleanPrefByKey
                    (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_IS_GCM_REG, true);
            Intent signinIntent = new Intent(this, SignInActivity.class);
            startActivity(signinIntent);
        } else {
            Log.e("Gok", "GCM reg fail");
        }
    }
}
