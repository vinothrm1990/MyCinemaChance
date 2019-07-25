package shadowws.in.mycinemachance.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import shadowws.in.mycinemachance.response.DirectorLoginResponse;
import shadowws.in.mycinemachance.response.DirectorRegisterResponse;
import shadowws.in.mycinemachance.response.MemberLoginResponse;
import shadowws.in.mycinemachance.response.MemberRegisterResponse;

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
}
