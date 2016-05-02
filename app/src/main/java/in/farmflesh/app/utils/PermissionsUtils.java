package in.farmflesh.app.utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RBP687 on 4/9/2016.
 */
public class PermissionsUtils {

    public static final String TAG = PermissionsUtils.class
            .getSimpleName();

    public static final int REQUEST_PERMISSION_CODE = 0;

    public static final String PERM_FRAG_TAG = "PERM_FRAG";
    public static final String REQ_PERMISSIONS = "REQ_PERM";
    public static final String RATIONALE = "RAT";

    /* Required Dangarous permissions */
    public static final String[] MUST_PERMISSIONS = {

            // Contacts Group
            "android.permission.GET_ACCOUNTS",

            // Phone group
            "android.permission.READ_PHONE_STATE",

            // Location Group
            "android.permission.ACCESS_COARSE_LOCATION",

            // Calender group
            "android.permission.READ_CALENDAR",

            // Manifest.permission.SEND_SMS

            //Write Storage
            // "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public static Map<String, Boolean> mPermissionsMap = new HashMap<String, Boolean>();

    /**
     * Untility function to request permission, passed activity will need to implement
     * PermissionsActivityInterface. Activity will be queried for permission required for
     * activity and only denied permissions will be extracted and requested. This util should be
     * used after checking for permission.
     *
     * @param activity Instance of activity requesting permissions
     */
    public static void requestPermissionsUtil(final Activity activity) {
        final String[] requiredPermissions = getDeniedPermissions(activity);
        requestPermissionsUtil(activity, requiredPermissions);
    }


    public static void requestPermissionsUtil(final Activity activity, String[] requiredPermissions) {

        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (!mPermissionsMap.get(permission)) {
                deniedPermissions.add(permission);
            }
        }
        String [] a_deniedPermissions = deniedPermissions.toArray(new
                String[deniedPermissions.size()]);
        for (String permission : deniedPermissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArray(REQ_PERMISSIONS, a_deniedPermissions);
                    bundle.putBoolean(RATIONALE, true);
                    showPermissionDialog(activity, bundle);
                    return;
                }
            }
        }
        if(deniedPermissions.size() == 0) {
            return;
        }

        ActivityCompat.requestPermissions(activity, a_deniedPermissions, REQUEST_PERMISSION_CODE);
    }

    /**
     * Untility function to check permission, passed activity will need to implement
     * PermissionsActivityInterface. Activity will be queried for permission required for
     * activity and a hashmap will be filled with grnated and denied permission, this hasmap will
     * be used later to request only denied permission.
     * It uses support lobrary apis so that any additioanl checks is wrapped and implemented by
     * android. To improve performance android version is checked at start of function so that
     * function returned if android version is less than M
     *
     * @param activity Instance of activity requesting permissions
     * @return true if all permissions are granted, false if even a single permission is not
     * granted.
     */
    public static boolean hasPermissions(Activity activity) {

        /*
        Support lib API will check for version but checking it here so that we dont call check
        permission api for all permissions if android version is less than M
        */
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Permissions request not needed for L & below");
            return true;
        }

        /*
        Check if activity is resuming after permission dialog, if yes
        then dont request permission again to avoid loop
        */
        if (isResumingFromPermission((PermissionsActivityInterface) activity)) {
            return true;
        }

        // Get permission array that needs to be request from activity
        String[] reqPermissions = ((PermissionsActivityInterface) activity)
                .getPermissionsRequestArray();

        return hasPermissions(activity, reqPermissions);
    }

    public static boolean hasPermissions(Context activity, String [] permissions) {
        boolean hasPermissions = true;
        mPermissionsMap.clear();

        for (String permission : permissions) {
            String message = permission + ": ";
            int i = ActivityCompat.checkSelfPermission(activity, permission);
            switch (i) {
                case PackageManager.PERMISSION_GRANTED:
                    mPermissionsMap.put(permission, true);
                    message += "Granted";
                    break;
                case PackageManager.PERMISSION_DENIED:
                    message += "Denied";
                    mPermissionsMap.put(permission, false);
                    hasPermissions = false;
                    break;
                default:
                    hasPermissions = false;
                    mPermissionsMap.put(permission, false);
                    message += "Unknown: " + i;
                    break;
            }
            Log.d(TAG, message);
        }
        return hasPermissions;
    }


    /**
     * Utility function to be called from activity call back function for permission result
     * onRequestPermissionsResult,
     *
     * @param requestCode  The request code passed in requestPermissions(String[], int)
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either
     *                     PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     * @return true if all requested permissions are greanted otherwise false
     */
    public static boolean onRequestPermissionsResultUtils(int requestCode, String[] permissions,
                                                          int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_CODE) {
            Log.d(TAG, "Invalid request code: " + requestCode);
            return false;
        }

        if (permissions == null || grantResults == null || permissions.length != grantResults
                .length || permissions.length == 0) {
            Log.d(TAG, "invalid inputs");
            return false;
        }

        for (int i = 0; i < permissions.length; i++) {
            switch (grantResults[i]) {
                case PackageManager.PERMISSION_GRANTED:
                    break;
                case PackageManager.PERMISSION_DENIED:
                    return false;
                default:
                    return false;
            }
        }
        return true;
    }

    /**
     * Function to check if activity is resuming after showing oermission request dialog or not,
     * this check is needed because when android permission dialog is shown activity is paused
     * and after user acts on permission request dialog (allow/deny) activity will resume , so
     * without this check activity will request permission again during resume and it will result
     * in infinite loop of requeting permission.
     * Activity will mark a flag true in onRequestPermissionsResult() and same flag will be
     * checked here.
     *
     * @param activity activity instance checking for permission during activity resume.
     * @return true if activity is resuming from permisison dialog , false if not.
     */
    private static boolean isResumingFromPermission(PermissionsActivityInterface
                                                            activity) {
        /*
        Check if activity is resuming after permission dialog, if yes
        then dont request permission again to avoid loop
        */
        if (activity.isResumingFromPermission()) {
            activity.setResumingFromPermission(false);
            return true;
        }
        return false;
    }

    /**
     * Utility function to show dialog with information to user to explain rationale or info if
     * user deny permission, this function will invoke PermissionsFragment which is a
     * DialogFragment.
     * Additional information can be passed as part of bundel to Dialog to cutomize it.
     *
     * @param activity activity instance to show permission dialog fragment
     * @param bundle   data passed to fragment as bundle
     */
    public static void showPermissionDialog(Activity activity, Bundle bundle) {

        // Check in Permission fragment is already shown, is yes then dismiss first
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag(PERM_FRAG_TAG);
        if (prev != null && prev instanceof DialogFragment) {
            DialogFragment dFrag = (DialogFragment) prev;
            dFrag.dismiss();
        }

        PermissionsFragment permFrag = new PermissionsFragment();
        permFrag.setArguments(bundle);
        permFrag.setCancelable(false);
        permFrag.show(ft, PERM_FRAG_TAG);
    }

    /**
     * Function to get  array of denied permissions by user
     *
     * @param activity reference to activity
     * @return array of denied permissions
     */
    private static String[] getDeniedPermissions(Activity activity) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        String[] reqPermissions = ((PermissionsActivityInterface) activity)
                .getPermissionsRequestArray();
        for (String permission : reqPermissions) {
            if (!mPermissionsMap.get(permission)) {
                deniedPermissions.add(permission);
            }
        }

        return deniedPermissions.toArray(new
                String[deniedPermissions.size()]);
    }

    /**
     * Interface that should be implemeted by Activities using PermissionsUtils functions
     */
    public interface PermissionsActivityInterface {

        // if activity is resuming from permisison dialog, set flag in callback
        // onRequestPermissionsResult
        public boolean isResumingFromPermission();

        // Function to set flag resuming from permission
        public void setResumingFromPermission(boolean val);

        // Function to get permisisons array an activity wants to request from user
        public String[] getPermissionsRequestArray();
    }

    /**
     * Interface that should be implemeted by watch related activities to get device type
     */
    public interface DeviceTypeInterface {
        /**
         * This function will indicate if current activty is related to watch so that Permisison
         * dialogs will show watch specific strings
         *
         * @return true if current activity is related to watch
         */
        public boolean isCurrentDeviceWatch();
    }
}
