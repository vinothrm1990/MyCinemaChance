package shadowws.in.mycinemachance.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DirectorPostResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
