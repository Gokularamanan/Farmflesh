package in.farmflesh.app.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import in.farmflesh.app.Main.Main2Activity;
import in.farmflesh.app.R;
import in.farmflesh.app.utils.SharedPreferenceFarm;

public class MainActivity extends Activity {

    private static final int PRIVACY_VIEW = 0;
    private static final int GMAIL_SIGNIN_VIEW = 1;
    private static final int GCM_SIGNIN_VIEW = 2;
    private static final int MAIN_VIEW = 3;
    private SharedPreferenceFarm sharedPreferenceFarm;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (!isBothRegUser()) {
            handleWelcomeScreenSqueuence();
            return;
        }else {
            Intent intent = new Intent(this, Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, MAIN_VIEW);
        }
    }

    private boolean isBothRegUser() {
        sharedPreferenceFarm = new SharedPreferenceFarm();
        return sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_IS_PRIVACY_ACCEPTED) &&
                sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                        SharedPreferenceFarm.PREFS_KEY_IS_GMAIL_REG) &&
                sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                        SharedPreferenceFarm.PREFS_KEY_IS_GCM_REG);
    }


    private void handleWelcomeScreenSqueuence() {
        sharedPreferenceFarm = new SharedPreferenceFarm();
        if (!sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_IS_PRIVACY_ACCEPTED)) {
            Intent i = new Intent(this, WelcomeActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            synchronized (this) {
                startActivityForResult(i, PRIVACY_VIEW);
            }
        } else if (!sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_IS_GMAIL_REG)) {
            Intent i = new Intent(this, GmailSignInActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            synchronized (this) {
                startActivityForResult(i, GMAIL_SIGNIN_VIEW);
            }
        } else if (!sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_IS_GCM_REG)) {
            Intent i = new Intent(this, GcmRegActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            synchronized (this) {
                startActivityForResult(i, GCM_SIGNIN_VIEW);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PRIVACY_VIEW:
                if (resultCode == RESULT_OK) {
                    Intent relaunch = new Intent(this, MainActivity.class);
                    //relaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(relaunch);
                }
                break;
            case GMAIL_SIGNIN_VIEW:
                if (resultCode == RESULT_OK) {
                    Intent relaunch = new Intent(this, MainActivity.class);
                    //relaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(relaunch);
                }
                break;
            case GCM_SIGNIN_VIEW:
                if (resultCode == RESULT_OK) {
                    Intent relaunch = new Intent(this, MainActivity.class);
                    //relaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(relaunch);
                }
                break;
            case MAIN_VIEW:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

/*    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem myAccount = menu.findItem(R.id.action_login);
        MenuItem myAccountSignOut = menu.findItem(R.id.action_logout);
        sharedPreferenceFarm = new SharedPreferenceFarm();
        if (sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                SharedPreferenceFarm.PREFS_KEY_IS_GMAIL_REG)) {
            myAccount.setTitle(R.string.my_account);
            myAccountSignOut.setVisible(true);
        } else {
            myAccount.setTitle(R.string.signed_in);
            myAccountSignOut.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_login) {
            sharedPreferenceFarm = new SharedPreferenceFarm();
            if (sharedPreferenceFarm.getBooleanPrefByKey(getApplicationContext(),
                    SharedPreferenceFarm.PREFS_KEY_IS_GMAIL_REG)) {
                Intent myAccountIntent = new Intent(this, MyAccountActivity.class);
                startActivity(myAccountIntent);
            } else {
                Intent signinIntent = new Intent(this, GmailSignInActivity.class);
                startActivity(signinIntent);
            }
        }
        if (id == R.id.action_logout) {
            Intent signinIntent = new Intent(this, GmailSignInActivity.class);
            signinIntent.putExtra("isLogoutFlow", true);
            startActivity(signinIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}