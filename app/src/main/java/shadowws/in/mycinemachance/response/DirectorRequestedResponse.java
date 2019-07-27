package shadowws.in.mycinemachance.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectorRequestedResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public Boolean getError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }

    public class User{


        @SerializedName("did")
        @Expose
        private String did;
        @SerializedName("dname")
        @Expose
        private String dname;
        @SerializedName("dmobile")
        @Expose
        private String dmobile;
        @SerializedName("demail")
        @Expose
        private String demail;
        @SerializedName("dlanguage")
        @Expose
        private String dlanguage;
        @SerializedName("mid")
        @Expose
        private String mid;
        @SerializedName("mfname")
        @Expose
        private String mfname;
        @SerializedName("mlname")
        @Expose
        private String mlname;
        @SerializedName("mmobile")
        @Expose
        private String mmobile;
        @SerializedName("memail")
        @Expose
        private String memail;
        @SerializedName("mlanguage")
        @Expose
        private String mlanguage;
        @SerializedName("mgender")
        @Expose
        private String mgender;
        @SerializedName("mindustry")
        @Expose
        private String mindustry;
        @SerializedName("mcategory")
        @Expose
        private String mcategory;
        @SerializedName("mprofile")
        @Expose
        private String mprofile;
        @SerializedName("mpicture")
        @Expose
        private String mpicture;
        @SerializedName("maudio")
        @Expose
        private String maudio;
        @SerializedName("mvideo")
        @Expose
        private String mvideo;
        @SerializedName("total_pages")
        @Expose
        private String totalPages;

        public String getDid() {
            return did;
        }

        public String getDname() {
            return dname;
        }

        public String getDmobile() {
            return dmobile;
        }

        public String getDemail() {
            return demail;
        }

        public String getDlanguage() {
            return dlanguage;
        }

        public String getMid() {
            return mid;
        }

        public String getMfname() {
            return mfname;
        }

        public String getMlname() {
            return mlname;
        }

        public String getMmobile() {
            return mmobile;
        }

        public String getMemail() {
            return memail;
        }

        public String getMlanguage() {
            return mlanguage;
        }

        public String getMgender() {
            return mgender;
        }

        public String getMindustry() {
            return mindustry;
        }

        public String getMprofile() {
            return mprofile;
        }

        public String getMpicture() {
            return mpicture;
        }

        public String getMaudio() {
            return maudio;
        }

        public String getMvideo() {
            return mvideo;
        }

        public String getTotalPages() {
            return totalPages;
        }

        public String getMcategory() {
            return mcategory;
        }
    }
}
