package com.example.arthome.newexchangeworld.ExchangeAPI;

import com.example.arthome.newexchangeworld.Models.AuthenticationModel;
import com.example.arthome.newexchangeworld.Models.ChatRoomMessageModel;
import com.example.arthome.newexchangeworld.Models.ChatRoomModel;
import com.example.arthome.newexchangeworld.Models.CreateExchangeModel;
import com.example.arthome.newexchangeworld.Models.CreateExchangeResponse;
import com.example.arthome.newexchangeworld.Models.EditGoodsModel;
import com.example.arthome.newexchangeworld.Models.ExchangeModel;
import com.example.arthome.newexchangeworld.Models.ExchangeRequestModel;
import com.example.arthome.newexchangeworld.Models.FaceBookUser;
import com.example.arthome.newexchangeworld.Models.GoodsModel;
import com.example.arthome.newexchangeworld.Models.PostModel;
import com.example.arthome.newexchangeworld.Models.QueueOfGoodsModel;
import com.example.arthome.newexchangeworld.Models.QueueRequestModel;
import com.example.arthome.newexchangeworld.Models.QueueResponseModel;
import com.example.arthome.newexchangeworld.Models.StarGoodsModel;
import com.example.arthome.newexchangeworld.Models.StarModel;
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
import retrofit2.http.Path;
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
    @POST("api/goods/post")
    //暫時不管ResponseType 故用ResponseBody即可
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
    Call<ResponseBody> editGoods(@Query("token") String strToken, @Body EditGoodsModel editGoodsModel);

    //exwd.csie.org:43002/api/queue/of/goods?host_goods_gid=119&token=0f2ed5277968c7dbb4175136c58d7e661e0220e3
    @GET("api/queue/of/goods")
    Call<List<QueueOfGoodsModel>> quequeOfGoods(@Query("host_goods_gid") int hostGoodsGID, @Query("token") String strToken);

    //exwd.csie.org:43002/api/exchange/of/user/all?owner_udi=2&token=
    //歷史紀錄
    @GET("api/exchange/of/user/all")
    Call<List<ExchangeModel>> historyExchange(@Query("owner_udi") int ownerID, @Query("token") String strToken);

    //exwd.csie.org:43002/api/queue/post?token=db80a7ef431dab870d2ae6f58027020f7918a56d
    @POST("api/queue/post")
    Call<QueueResponseModel> quequeGoods(@Query("token") String strToken, @Body QueueRequestModel queueRequestModel);

    //exwd.csie.org:43002/api/user/me/goods/queue?token=019b04aea862b35b0ae0aaa18c76208b77355631
    @GET("api/user/me/goods/queue")
    Call<List<ExchangeRequestModel>> getMyExchangeRequest(@Query("token") String strToken);

    //api/exchange/create( goods_one_gid, goods_two_gid )
    @POST("api/exchange/create")
    Call<CreateExchangeResponse> createExchange(@Query("token") String strToken, @Body CreateExchangeModel createExchangeModel);

    //http://exwd.csie.org:43002/api/star?token=853
    @POST("api/star")
    Call<StarModel> starGoods(@Query("token") String strToken, @Body StarGoodsModel starGoodsModel);

    //exwd.csie.org:43002/api/star/by?starring_user_uid=5
    @GET("api/star/by")
    Call<List<StarModel>> getUserWishList(@Query("starring_user_uid") int UID);

    //exwd.csie.org:43002/api/exchange/agree?eid=7&owner_uid=2&token=853bc0f295c5a6649f02943e14471f9066df8fe0
    @PUT("api/exchange/agree")
    Call<CreateExchangeResponse> agreeExchange(@Query("eid") int EID, @Query("owner_uid") int ownerUID, @Query("token") String StrToken);

    @PUT("api/exchange/drop")
    Call<CreateExchangeResponse> dropExchange(@Query("eid") int EID, @Query("token") String StrToken);

    //exwd.csie.org:43002/api/chatroom/with/8?token=853bc0f295c5a6649f02943e14471f9066df8fe0
    @GET("api/chatroom/with/{uid}")
    Call<ChatRoomModel> getChatRoom(@Path("uid") String other_uid, @Query("token") String StrToken);

    //exwd.csie.org:43002/api/message/of/chatroom?chatroom_cid=3&limit=30&offset=0&token=7
    @GET("api/message/of/chatroom")
    Call<List<ChatRoomMessageModel>> getMessage(@Query("chatroom_cid") int cid, @Query("limit") int limit,@Query("offset") int offset, @Query("token") String strToken);
}
