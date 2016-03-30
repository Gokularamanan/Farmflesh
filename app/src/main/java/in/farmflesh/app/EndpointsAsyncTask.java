package in.farmflesh.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.GsonBuilder;

import java.io.IOException;

import in.farmflesh.app.utils.AsyncResponse;
import in.farmflesh.app.utils.RegistrationRecord;
import in.farmflesh.backend.myApi.MyApi;

/**
 * Created by RBP687 on 3/21/2016.
 */
public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    public AsyncResponse delegate = null;

    private ProgressDialog dialog;

    public EndpointsAsyncTask(Activity activity) {
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Doing something, please wait.");
        dialog.show();
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://farmflesh.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        context = params[0].first;
        //String name = params[0].second;
        String email = params[0].second;

        try {
            return myApiService.takeEmail(email).execute().getData();
            //return myApiService.takeEmail(reg).execute().getData();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        delegate.processFinish(result);
        Log.i("Gok", result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
