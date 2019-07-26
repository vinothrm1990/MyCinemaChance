package shadowws.in.mycinemachance.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GuestCategoryResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public Boolean getError() {
        return error;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private String image;

        public Integer getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }
    }
}
