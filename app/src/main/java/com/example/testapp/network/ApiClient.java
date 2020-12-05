package com.example.testapp.network;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.testapp.BuildConfig;
import com.example.testapp.MyApplication;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        File httpCacheDirectory = new File(MyApplication.getContext().getCacheDir(), "offlineCache");

        //10 MB
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(s -> Log.d("Retrofit: OkHttp:", s));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
//                            .header("Cache-Control", cacheControl.toString())
                            .build();
                })
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    try {
                        return chain.proceed(original);
                    } catch (Exception e) {

                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxStale(30, TimeUnit.DAYS)
                                .build();

                        Request request = original.newBuilder()
                                .header("Cache-Control", cacheControl.toString())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        OkHttpClient client = ApiClient.trustAllSslClient(okHttpClient);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }


    @SuppressLint("TrustAllX509TrustManager")
    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };


    private static final SSLContext trustAllSslContext;

    static {
        try {
            trustAllSslContext = SSLContext.getInstance("SSL");
            trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

    private static OkHttpClient trustAllSslClient(OkHttpClient client) {
        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
        return builder.build();
    }
}
