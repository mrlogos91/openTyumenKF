package com.opentmn.opentmn.network.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.opentmn.opentmn.model.Answer;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.Page;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.Round;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.model.UsersCount;
import com.opentmn.opentmn.model.VisibleResults;
import com.opentmn.opentmn.network.model.ApiMethod;
import com.opentmn.opentmn.network.model.ApiResponseModel;

import java.util.List;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public interface MyTyumenService {

    @FormUrlEncoded
    @POST(ApiMethod.USERS)
    rx.Observable<ApiResponseModel<User>> registration(@NonNull @Field("email") String email,
                                                       @NonNull @Field("password") String password,
                                                       @NonNull @Field("device_uid") String device_uid,
                                                       @NonNull @Field("device_type") String device_type);

    @FormUrlEncoded
    @PUT(ApiMethod.PASSWORD_RECOVERY)
    rx.Observable<ApiResponseModel> restore(@NonNull @Field("email") String email);

    @FormUrlEncoded
    @POST(ApiMethod.AUTH)
    rx.Observable<ApiResponseModel<User>> auth(@NonNull @Field("email") String email,
                                         @NonNull @Field("password") String password,
                                         @NonNull @Field("device_uid") String device_uid,
                                         @NonNull @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST(ApiMethod.USERS)
    rx.Observable<ApiResponseModel<User>> authSocial(@NonNull @Field("social_type") String social_type,
                                                     @NonNull @Field("social_id") String social_id,
                                                     @NonNull @Field("social_token") String social_token,
                                                     @NonNull @Field("device_uid") String device_uid,
                                                     @NonNull @Field("device_type") String device_type,
                                                     @Nullable @Field("captcha_sid") String captchaSid,
                                                     @Nullable @Field("captcha_key") String captchaKey);

    @GET(ApiMethod.CATEGORY)
    rx.Observable<ApiResponseModel<List<Category>>> categories(@Nullable @Query("all") Integer all, @NonNull @Query("token") String token);

    @GET(ApiMethod.USERS)
    rx.Observable<ApiResponseModel<List<User>>> users(@NonNull @Query("token") String token,
                                                      @Query("page") int page,
                                                      @Query("per_page") int perPage,
                                                      @Query("query") String query,
                                                      @Query("only_friends") int onlyFriends,
                                                      @Query("only_blocks") int onlyBlocks);

    @DELETE(ApiMethod.AUTH)
    rx.Observable<ApiResponseModel> signOut();

    @FormUrlEncoded
    @POST(ApiMethod.GAMES)
    rx.Observable<ApiResponseModel<Game>> createGame(@Field("game_type_id") int gameTypeId,
                                                     @NonNull @Field("token") String token);

    @FormUrlEncoded
    @POST(ApiMethod.GAMES)
    rx.Observable<ApiResponseModel<Game>> createGameAgainstUser(@Field("game_type_id") int gameTypeId,
                                                                @Field("user_follower_id") int userFollowerId,
                                                                @NonNull @Field("token") String token);

    @GET(ApiMethod.GAMES)
    rx.Observable<ApiResponseModel<List<Game>>> games(@Query("is_finished") int isFinished,
                                                      @Query("page") int page,
                                                      @Query("per_page") int perPage,
                                                      @NonNull @Query("token") String token);

    @GET(ApiMethod.GAMES)
    rx.Observable<ApiResponseModel<List<Game>>> gamesHistory(@Query("is_finished") int isFinished,
                                                             @Query("page") int page,
                                                             @Query("per_page") int perPage,
                                                             @Nullable @Query("is_winner") Integer isWinner,
                                                             @Nullable @Query("is_loser") Integer isLoser,
                                                             @Nullable @Query("is_draw") Integer isDraw,
                                                             @NonNull @Query("token") String token);

    @GET(ApiMethod.GAMES + "/{game_id}")
    rx.Observable<ApiResponseModel<Game>> game(@Path("game_id") int gameId,
                                               @NonNull @Query("token") String token);

    @FormUrlEncoded
    @PUT(ApiMethod.GAMES)
    rx.Observable<ApiResponseModel<Game>> updateGame(@Field("game_id") int gameId,
                                                     @Field("is_approved") int isApproved,
                                                     @NonNull @Field("token") String token);

    @FormUrlEncoded
    @PUT(ApiMethod.GAMES + "/{game_id}/close")
    rx.Observable<ApiResponseModel<Void>> closeGame(@Path("game_id") int gameId,
                                                     @NonNull @Field("token") String token);

    @FormUrlEncoded
    @POST(ApiMethod.ROUNDS)
    rx.Observable<ApiResponseModel<Game>> createRound(@Field("game_id") int gameId,
                                                       @Field("category_id") int categoryId,
                                                       @NonNull @Field("token") String token);

    @GET(ApiMethod.QUESTIONS)
    rx.Observable<ApiResponseModel<List<Question>>> questions(@Query("category_id") int categoryId,
                                                                @NonNull @Query("token") String token);

    @Multipart
    @POST(ApiMethod.QUESTIONS)
    rx.Observable<ApiResponseModel<Void>> sendQuestion(@Part("name") RequestBody name,
                                                       @Part("category_id") RequestBody categoryId,
                                                       @Part("answer_true_1") RequestBody answer1,
                                                       @Part("answer_false_2") RequestBody answer2,
                                                       @Part("answer_false_3") RequestBody answer3,
                                                       @Part("answer_false_4") RequestBody answer4,
                                                       @Part("source") RequestBody source,
                                                       @Part("author") RequestBody author,
                                                       @Part("reference") RequestBody reference,
                                                       @Part("rights") RequestBody rights,
                                                       @Part MultipartBody.Part file,
                                                       @Part("token") RequestBody token);

    @FormUrlEncoded
    @POST(ApiMethod.ANSWERS)
    rx.Observable<ApiResponseModel<Game>> sendAnswer(@Field("game_round_id") int roundId,
                                                       @Field("question_id") int questionId,
                                                       @NonNull @Field("answer_id") String answerId,
                                                       @NonNull @Field("token") String token);

    @GET(ApiMethod.GIFTS)
    rx.Observable<ApiResponseModel<List<Gift>>> gifts(@NonNull @Query("token") String token, @Query("page") int page, @Query("per_page") int perPage);

    @GET(ApiMethod.USERS + "/{token}")
    rx.Observable<ApiResponseModel<User>> profile(@NonNull @Path("token") String token);

    @GET(ApiMethod.USERS + "/count")
    rx.Observable<ApiResponseModel<UsersCount>> usersCount();

    @GET(ApiMethod.USERS)
    rx.Observable<ApiResponseModel<List<User>>> userWithId(@Query("user_id") int userId, @NonNull @Query("token") String token);

    @FormUrlEncoded
    @POST(ApiMethod.FRIENDS)
    rx.Observable<ApiResponseModel<Void>> addFriend(@Field("user_id") int userId, @NonNull @Field("token") String token);

    @DELETE(ApiMethod.FRIENDS)
    rx.Observable<ApiResponseModel<Void>> deleteFriend(@Query("user_id") int userId, @NonNull @Query("token") String token);

    @GET(ApiMethod.RESULTS + "/{game_id}")
    rx.Observable<ApiResponseModel<GameResult>> result(@Path("game_id") int gameId, @Query("is_surrender") int isSurrender, @NonNull @Query("token") String token);

    @GET(ApiMethod.PAGES + "/{alias}")
    rx.Observable<ApiResponseModel<Page>> page(@Path("alias") String alias);

    @GET(ApiMethod.SOCIALS)
    rx.Observable<ApiResponseModel<List<SocialUser>>> socials(@Query("token") String token, @Query("social_id") String socialId, @Query("social_token") String socialToken, @Query("captcha_sid") String captchaSid, @Query("captcha_key") String captchaKey);

    @GET(ApiMethod.VISIBLE_RESULTS)
    rx.Observable<ApiResponseModel<VisibleResults>> visibleResults(@Query("token") String token);

    @FormUrlEncoded
    @POST(ApiMethod.BLOCKS)
    rx.Observable<ApiResponseModel<Void>> block(@Field("user_id") int userId, @Field("token") String token);

    @DELETE(ApiMethod.BLOCKS)
    rx.Observable<ApiResponseModel<Void>> delBlock(@Query("user_id") int userId, @Query("token") String token);

    @Multipart
    @PUT(ApiMethod.USERS)
    rx.Observable<ApiResponseModel<Void>> updateProfile(@Part("token") RequestBody token,
                                                        @Part("old_password") RequestBody oldPassword,
                                                        @Part("new_password") RequestBody newPassword,
                                                        @Part("gender_id") RequestBody genderId,
                                                        @Part("name") RequestBody name,
                                                        @Part("email") RequestBody email,
                                                        @Part MultipartBody.Part avatar);

    @FormUrlEncoded
    @POST(ApiMethod.ORDERS)
    rx.Observable<ApiResponseModel<User>> orders(@Field("token") String token, @Field("gift_id") int giftId, @Field("email") String email);

    @FormUrlEncoded
    @POST(ApiMethod.SMILES)
    rx.Observable<ApiResponseModel<Void>> sendSmile(@Field("token") String token, @Field("game_id") int gameId, @Field("smile_id") int smileId);

    @FormUrlEncoded
    @POST(ApiMethod.NOTIFICATIONS)
    rx.Observable<ApiResponseModel<Void>> addToken(@Field("token") String token, @Field("device_token") String deviceToken);

    @DELETE(ApiMethod.NOTIFICATIONS)
    rx.Observable<ApiResponseModel<Void>> delToken(@Query("token") String token);
}
