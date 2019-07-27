package shadowws.in.mycinemachance.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import shadowws.in.mycinemachance.response.DirectorDataResponse;
import shadowws.in.mycinemachance.response.DirectorLoginResponse;
import shadowws.in.mycinemachance.response.DirectorMemberResponse;
import shadowws.in.mycinemachance.response.DirectorRequestedResponse;
import shadowws.in.mycinemachance.response.DirectorPasswordResponse;
import shadowws.in.mycinemachance.response.DirectorPostResponse;
import shadowws.in.mycinemachance.response.DirectorRegisterResponse;
import shadowws.in.mycinemachance.response.GuestCategoryResponse;
import shadowws.in.mycinemachance.response.GuestDataResponse;
import shadowws.in.mycinemachance.response.MemberLoginResponse;
import shadowws.in.mycinemachance.response.MemberRegisterResponse;
import shadowws.in.mycinemachance.response.UploadResponse;

public interface RetrofitAPI {

    @FormUrlEncoded
    @POST("memberlogin")
    Call<MemberLoginResponse> memberLogin(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("directorlogin")
    Call<DirectorLoginResponse> directorLogin(
            @Field("mobile") String mobile,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("createmember")
    Call<MemberRegisterResponse> memberRegister(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("language") String language,
            @Field("category") String category,
            @Field("profession") String profession,
            @Field("fb") String fb,
            @Field("dob") String dob,
            @Field("gender") String gender,
            @Field("qualification") String qualification,
            @Field("address") String address,
            @Field("state") String state,
            @Field("actor") String actor,
            @Field("industry") String industry,
            @Field("picture") String picture,
            @Field("resume") String resume,
            @Field("audio") String audio,
            @Field("video") String video,
            @Field("achievement") String achievement,
            @Field("yourself") String yourself,
            @Field("memdate") String memdate
    );

    @FormUrlEncoded
    @POST("createdirector")
    Call<DirectorRegisterResponse> directorRegister(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("password") String password,
            @Field("language") String language,
            @Field("category") String category,
            @Field("confirm") String confirm
    );


    @GET("guestdata")
    Call<GuestDataResponse> guestData();

    @GET("guestcategory")
    Call<GuestCategoryResponse> guestCategory();

    @Multipart
    @POST("uploadfile")
    Call<UploadResponse> uploadFile(
            @Part MultipartBody.Part file,
            @Part("file") RequestBody name
    );

    @FormUrlEncoded
    @POST("allmembers")
    Call<DirectorDataResponse> directorData(
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("createpost")
    Call<DirectorPostResponse> directorPost(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("language") String language,
            @Field("category") String category,
            @Field("title") String title,
            @Field("desc") String desc,
            @Field("did") String did,
            @Field("status") int status
    );

    @FormUrlEncoded
    @POST("updatepassword")
    Call<DirectorPasswordResponse> updatePassword(
            @Field("currentpassword") String currentpassword,
            @Field("newpassword") String newpassword,
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("allrequestedmembers")
    Call<DirectorRequestedResponse> allRequestedMember(
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("requestedmember")
    Call<DirectorMemberResponse> requestedMember(
            @Field("mobile") String mobile
    );
}
