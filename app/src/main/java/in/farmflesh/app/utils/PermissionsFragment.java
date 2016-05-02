package in.farmflesh.app.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import in.farmflesh.app.R;
import in.farmflesh.app.Welcome.WelcomeActivity;

/**
 * Created by RBP687 on 4/9/2016.
 */
public class PermissionsFragment extends DialogFragment {

    public static final String TAG = PermissionsFragment.class
            .getSimpleName();

    Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        boolean isRationale = false;
        if (bundle != null) {
            isRationale = bundle.getBoolean(PermissionsUtils.RATIONALE);
        } else {
            return null;
        }

        AlertDialog.Builder permissionAlert = new AlertDialog.Builder(mActivity);

        // disable cancel on press back button
        permissionAlert.setCancelable(false);
        // Check if we are showing permission rationale info or info after user deny permissions
        if (isRationale) {
            final String[] requiredPermissions = bundle.getStringArray(PermissionsUtils
                    .REQ_PERMISSIONS);
            // Set Dialog Title
            permissionAlert.setTitle((mActivity.getString(R.string.perm_req_title)));
            permissionAlert.setMessage(buildMessageBody(mActivity, requiredPermissions));
            // Positive button
            permissionAlert.setPositiveButton(mActivity.getString(R.string.cont), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, requiredPermissions,
                                    PermissionsUtils.REQUEST_PERMISSION_CODE);
                            dialog.dismiss();
                        }
                    });

        } else {

            // Set Dialog Title
            permissionAlert.setTitle(mActivity.getString(R.string.perm_req_title));
            // Set Dialog Message
            permissionAlert.setMessage(mActivity.getString(R.string.perm_denied));

            // Positive button
            permissionAlert.setPositiveButton(mActivity.getString(R.string.app_info), new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startApplicationInfo();
                        }
                    });
        }
        return permissionAlert.create();
    }

    private void startApplicationInfo() {
        try {
            // Start Moto Connect application info screen
            Intent intent = new Intent(android.provider.Settings
                    .ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            // Should not come here , anyway start manage application screen
            Intent intent = new Intent(android.provider.Settings
                    .ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
    }

    private String buildMessageBody(Activity activity, String[] requiredPermissions) {
        StringBuilder msgStr = new StringBuilder();
        if(activity instanceof WelcomeActivity) {
            msgStr.append(mActivity.getString(R.string.perm_req_sms_header));
        }else {
            msgStr.append(mActivity.getString(R.string.perm_req_body_header));
        }

        PackageManager pm = activity.getPackageManager();
        for (String permission : requiredPermissions) {
            try {
                PermissionInfo pInfo = pm.getPermissionInfo(permission, 0);
                switch (pInfo.group) {
                    case Manifest.permission_group.LOCATION:
                        msgStr.append(mActivity.getString(R.string.perm_req_body_location));
                        break;
                    case Manifest.permission_group.CONTACTS:
                        msgStr.append(mActivity.getString(R.string.perm_req_body_contacts));
                        break;
                    case Manifest.permission_group.PHONE:
                        msgStr.append(mActivity.getString(R.string.perm_req_body_phone));
                        break;
                    case Manifest.permission_group.CALENDAR:
                        msgStr.append(mActivity.getString(R.string.perm_req_body_calendar));
                        break;
                    case Manifest.permission_group.STORAGE:
                        msgStr.append(mActivity.getString(R.string.perm_req_loop_storage));
                        break;
                    case Manifest.permission_group.SMS:
                        msgStr.append(mActivity.getString(R.string.perm_req_loop_sms));
                        break;
                    default:
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        msgStr.append(mActivity.getString(R.string.perm_req_body_footer));

        return msgStr.toString();
    }
}

