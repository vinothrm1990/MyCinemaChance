package shadowws.in.mycinemachance.retrofit;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import thebat.lib.validutil.ValidUtils;

public class RetrofitBASE {

    private static Retrofit retrofit = null;
    private final static long CACHE_SIZE = 10 * 1024 * 1024;
    static ValidUtils validUtils = new ValidUtils();

    private static OkHttpClient buildClient(Context context) {

        // Build interceptor
        final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (validUtils.isNetworkAvailable(context)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };

        // Create Cache
        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        return new OkHttpClient
                .Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .build();
    }

    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(buildClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://mycinemachance.com/jsons/api/")
                    .build();
        }
        return retrofit;
    }
}
