package in.farmflesh.app.Welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import in.farmflesh.app.R;
import in.farmflesh.app.utils.AsyncResponse;
import in.farmflesh.app.utils.SharedPreferenceFarm;

public class GcmRegActivity extends Activity implements AsyncResponse {
    private SharedPreferenceFarm sharedPreferenceFarm;

    private static final String TAG = "GcmRegActivity";
    private static final String RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferenceFarm = new SharedPreferenceFarm();

        Button button = (Button) findViewById(R.id.signIn_welcome);
        final EndpointsAsyncTask asyncTask = new EndpointsAsyncTask(GcmRegActivity.this);
        asyncTask.delegate = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = sharedPreferenceFarm.getStringPrefByKey
                        (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_EMAIL);
                String emailName = sharedPreferenceFarm.getStringPrefByKey
                        (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_NAME);
                Log.d(TAG, "emailId:" +emailId);
                Log.d(TAG, "emailName:" +emailName);
                if (asyncTask.getStatus() == AsyncTask.Status.PENDING || (emailId != null)) {
                    //execute the async task
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("email", emailId);
                        jo.put("name", emailName);
                        jo.put("phone", "9901814275");
                        asyncTask.execute(new Pair<Context, String>(GcmRegActivity.this, jo.toString()));
                    }catch(JSONException ex){
                        Log.d(TAG, ex.toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Already GCM registered", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        sharedPreferenceFarm.saveBooleanPrefByKey
                (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_IS_GCM_REG, true);
        Log.d(TAG, "GCM reg success");
        Intent resultData = new Intent();
        resultData.putExtra(RESULT, true);
        setResult(RESULT_OK, resultData);
        finish();
        /*if (output.equals("Hi, Manfred")) {
            Log.d("Gok", "GCM reg success");
            sharedPreferenceFarm.saveBooleanPrefByKey
                    (getApplicationContext(), SharedPreferenceFarm.PREFS_KEY_IS_GCM_REG, true);
            Intent signinIntent = new Intent(this, GmailSignInActivity.class);
            startActivity(signinIntent);
        } else {
            Log.e("Gok", "GCM reg fail");
        }*/
    }
}
