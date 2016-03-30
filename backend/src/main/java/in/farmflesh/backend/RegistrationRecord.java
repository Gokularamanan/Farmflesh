package in.farmflesh.backend;

import com.google.appengine.repackaged.com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by RBP687 on 3/30/2016.
 */
public class RegistrationRecord {

    @Index
    private String regId;
    // you can add more fields...

    @Id
    private Long userId;

    @Index
    private String phoneNumber;

    @Index
    private String accountID;

    private String name;

    private String mmUrl;

    private int versionCode = -1;

    @SerializedName("gcmregId")
    private String gcmregId;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phoneNumber;
    }
}
