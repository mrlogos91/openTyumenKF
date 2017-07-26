package com.opentmn.opentmn.data.repository;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public interface MyTyumenRepository {

    Observable<ApiResponseModel<User>> registration(@NonNull  String email, @NonNull String password);

    Observable<ApiResponseModel> restore(@NonNull String email);

    Observable<ApiResponseModel> signOut();

    Observable<ApiResponseModel<User>> authenticate(@NonNull String socialType, @NonNull  String userId, @NonNull  String token, @Nullable String captchaSid, @Nullable String captchaKey);

    Observable<ApiResponseModel<User>> authenticate(@NonNull  String email, @NonNull String pass);

    Observable<ApiResponseModel<List<Category>>> categories(@Nullable Integer all, @NonNull String token);

    Observable<ApiResponseModel<List<User>>> users(@NonNull String token, int page, int perPage, String query, int onlyFriends, int onlyBlocks);

    Observable<ApiResponseModel<Game>> createGame(int gameTypeId, @NonNull String token);

    Observable<ApiResponseModel<Game>> createGameAgainstUser(int userFollowerId, String token);

    Observable<ApiResponseModel<List<Game>>> games(int isFinished, int page, int perPage, @NonNull String token);

    Observable<ApiResponseModel<List<Game>>> gamesHistory(int page, int perPage, @Nullable Integer isWinner, @Nullable Integer isLoser, @Nullable Integer isDraw, @NonNull String token);

    Observable<ApiResponseModel<Game>> game(int gameId, String token);

    Observable<ApiResponseModel<Game>> updateGame(int gameId, int isApproved, String token);

    Observable<ApiResponseModel<Void>> closeGame(int gameId, String token);

    Observable<ApiResponseModel<Game>> createRound(int gameId, int categoryId, @NonNull String token);

    Observable<ApiResponseModel<List<Question>>> questions(int categoryId, @NonNull String token);

    rx.Observable<ApiResponseModel<Void>> sendQuestion(String name, int categoryId, String answer1, String answer2, String answer3, String answer4, String source, String author, String reference, String rights, String filePath, String token);

    Observable<ApiResponseModel<List<User>>> friends(@NonNull String token, int page, int perPage, String query);

    Observable<ApiResponseModel<List<Gift>>> gifts(@NonNull String token, int page, int perPage);

    Observable<ApiResponseModel<User>> profile(@NonNull String token);

    Observable<ApiResponseModel<UsersCount>> usersCount();

    Observable<ApiResponseModel<List<User>>> userWithId(int userId, String token);

    Observable<ApiResponseModel<Void>> addFriend(int userId, @NonNull String token);

    Observable<ApiResponseModel<Void>> deleteFriend(int userId, String token);

    Observable<ApiResponseModel<Game>> sendAnswer(int roundId, int questionId, String answerId, String token);

    Observable<ApiResponseModel<GameResult>> result(int gameId, int isSurrender, String token);

    Observable<ApiResponseModel<Page>> page(String alias);

    Observable<ApiResponseModel<List<SocialUser>>> socials(String token, String socialId, String socialToken, String captchaSid, String captchaKey);

    Observable<ApiResponseModel<VisibleResults>> visibleResults(String token);

    Observable<ApiResponseModel<Void>> block(int userId, String token);

    Observable<ApiResponseModel<Void>> delBlock(int userId, String token);

    Observable<ApiResponseModel<Void>> updateProfile(String token, String oldPassword, String newPassword, String genderId, String name, String email, String avatar);

    Observable<ApiResponseModel<User>> orders(String token, int giftId, String email);

    Observable<ApiResponseModel<Void>> sendSmile(String token, int gameId, int smileId);

    Observable<ApiResponseModel<Void>> addToken(String token, String deviceToken);

    Observable<ApiResponseModel<Void>> delToken(String token);
}
