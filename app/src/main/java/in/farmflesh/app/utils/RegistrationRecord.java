package in.farmflesh.app.utils;

import com.google.appengine.repackaged.com.google.gson.annotations.SerializedName;


/**
 * Created by RBP687 on 3/30/2016.
 */
public class RegistrationRecord {

    @SerializedName("regId")
    private String regId;
    // you can add more fields...

    @SerializedName("userId")
    private Long userId;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    private String name;

    private String phone;

    private String email;

    private int versionCode = -1;

    @SerializedName("gcmregId")
    private String gcmregId;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return name;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.name = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
