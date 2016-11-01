package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.UploadImageModel;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.google.android.gms.auth.api.Auth;

import java.util.List;
import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by SSD on 2016/10/15.
 */
public interface ExchangeService {

    // GET api/user?uid={int}&identity={string}
        @GET("api/user")
        Call<UserModel> getUserInfo(@Query("uid") int intUID, @Query("identity") String strIdentity);

    @POST("api/authenticate/login")
    Call<AuthenticationModel> getToken(@Body FaceBookUser faceBookUser);

    //http://exwd.csie.org:43002/api/upload/image?token=
    @POST("api/upload/image")
    Call<ResponseBody> upLoadImage(@Query("token") String strToken, @Body UploadImageModel uploadImageModel);

    //http://exwd.csie.org:43002/api/upload/image?token=
    @POST("api/upload/image")
    rx.Observable<ResponseBody> upLoadImageRxJava(@Query("token") String strToken, @Body UploadImageModel uploadImageModel);

    //api/goods/post?token=
    @POST("api/goods/post")     //暫時不管ResponseType 故用ResponseBody即可
    Call<ResponseBody> upLoadGoods(@Query("token") String strToken, @Body PostModel postModel);

    @GET("api/goods/search")
    Call<List<GoodsModel>> downloadGoods();

    //http://exwd.csie.org:43002/api/goods/search?category={}
    @GET("api/goods/search")
    Call<List<GoodsModel>> downloadCategoryGoods(@Query("category") String strCategory);

}
