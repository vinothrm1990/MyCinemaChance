package shadowws.in.mycinemachance.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectorDataResponse {

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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("fname")
        @Expose
        private String fname;
        @SerializedName("lname")
        @Expose
        private String lname;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("profession")
        @Expose
        private String profession;
        @SerializedName("fb")
        @Expose
        private String fb;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("qualification")
        @Expose
        private String qualification;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("actor")
        @Expose
        private String actor;
        @SerializedName("industry")
        @Expose
        private String industry;
        @SerializedName("profile")
        @Expose
        private String profile;
        @SerializedName("picture")
        @Expose
        private String picture;
        @SerializedName("resume")
        @Expose
        private String resume;
        @SerializedName("audio")
        @Expose
        private String audio;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("achievement")
        @Expose
        private String achievement;
        @SerializedName("yourself")
        @Expose
        private String yourself;
        @SerializedName("memdate")
        @Expose
        private String memdate;
        @SerializedName("total_pages")
        @Expose
        private String total_pages;

        public Integer getId() {
            return id;
        }

        public String getFname() {
            return fname;
        }

        public String getLname() {
            return lname;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }

        public String getLanguage() {
            return language;
        }

        public String getCategory() {
            return category;
        }

        public String getProfession() {
            return profession;
        }

        public String getFb() {
            return fb;
        }

        public String getDob() {
            return dob;
        }

        public String getGender() {
            return gender;
        }

        public String getQualification() {
            return qualification;
        }

        public String getAddress() {
            return address;
        }

        public String getState() {
            return state;
        }

        public String getActor() {
            return actor;
        }

        public String getIndustry() {
            return industry;
        }

        public String getPicture() {
            return picture;
        }

        public String getResume() {
            return resume;
        }

        public String getAudio() {
            return audio;
        }

        public String getVideo() {
            return video;
        }

        public String getAchievement() {
            return achievement;
        }

        public String getYourself() {
            return yourself;
        }

        public String getMemdate() {
            return memdate;
        }

        public String getTotal_pages() {
            return total_pages;
        }

        public String getProfile() {
            return profile;
        }
    }
}
