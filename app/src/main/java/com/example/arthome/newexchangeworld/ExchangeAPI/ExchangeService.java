package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.EditGoodsModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.Models.UploadImageModel;
import com.example.arthome.newexchangeworld.Models.UserModel;
import com.google.android.gms.auth.api.Auth;

import java.util.List;
import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    //exwd.csie.org:43002/api/goods/of?owner_uid=2
    @GET("api/goods/of")
    Call<List<GoodsModel>> downloadMyGoods(@Query("owner_uid") int strOwnerUID);

    //exwd.csie.org:43002/api/goods/delete?gid=131&token=1d1f66cb89bf2f19b6e12b35a6c36f8cd1388cdb
    @DELETE("api/goods/delete")
    Call<ResponseBody> deleteGoods(@Query("gid") int gid, @Query("token") String strToken);

    //exwd.csie.org:43002/api/goods/edit?token=75cd749ae06b15ecf3817c64a83764b22ebea5ad
    @PUT("api/goods/edit")
    Call<ResponseBody> editGoods(@Query("token") String strToken,@Body EditGoodsModel editGoodsModel);

    //exwd.csie.org:43002/api/queue/of/goods?host_goods_gid=119&token=0f2ed5277968c7dbb4175136c58d7e661e0220e3
    @GET("api/queue/of/goods")
    Call<List<QueueOfGoodsModel>> quequeOfGoods(@Query("host_goods_gid") int hostGoodsGID, @Query("token") String strToken);

}
