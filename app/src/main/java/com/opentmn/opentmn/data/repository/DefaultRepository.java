package com.opentmn.opentmn.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.opentmn.opentmn.data.RepositoryProvider;
import com.opentmn.opentmn.model.Category;
import com.opentmn.opentmn.model.Game;
import com.opentmn.opentmn.model.GameResult;
import com.opentmn.opentmn.model.GameType;
import com.opentmn.opentmn.model.Gift;
import com.opentmn.opentmn.model.Page;
import com.opentmn.opentmn.model.Question;
import com.opentmn.opentmn.model.SocialUser;
import com.opentmn.opentmn.model.User;
import com.opentmn.opentmn.model.UsersCount;
import com.opentmn.opentmn.model.VisibleResults;
import com.opentmn.opentmn.network.model.ApiResponseModel;
import com.opentmn.opentmn.network.retrofit.ApiFactory;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

import static com.opentmn.opentmn.Config.DEVICE_TYPE;

/**
 * Created by Alexey Antonchik on 11.12.16.
 */

public class DefaultRepository implements MyTyumenRepository {

    @Override
    public Observable<ApiResponseModel<User>> registration(@NonNull String email, @NonNull String password) {
        return ApiFactory.getService().registration(email, password, RepositoryProvider.provideKeyValueStorage().getUIID(), DEVICE_TYPE);
    }

    @Override
    public Observable<ApiResponseModel> restore(@NonNull String email) {
        return ApiFactory.getService().restore(email);
    }

    @Override
    public Observable<ApiResponseModel> signOut() {
        return ApiFactory.getService().signOut();
    }

    @Override
    public Observable<ApiResponseModel<User>> authenticate(@NonNull String socialType, @NonNull String userId, @NonNull String token, @Nullable String captchaSid, @Nullable String captchaKey) {
        return ApiFactory.getService().authSocial(socialType, userId, token, RepositoryProvider.provideKeyValueStorage().getUIID(), DEVICE_TYPE, captchaSid, captchaKey);
    }

    @Override
    public Observable<ApiResponseModel<User>> authenticate(@NonNull String email, @NonNull String pass) {
        return ApiFactory.getService().auth(email, pass, RepositoryProvider.provideKeyValueStorage().getUIID(), DEVICE_TYPE);
    }

    @Override
    public Observable<ApiResponseModel<List<Category>>> categories(@Nullable Integer all, @NonNull String token) {
        return ApiFactory.getService().categories(all, token);
    }

    @Override
    public Observable<ApiResponseModel<List<User>>> users(@NonNull String token, int page, int perPage, String query, int onlyFriends, int onlyBlocks) {
        return ApiFactory.getService().users(token, page, perPage, query, onlyFriends, onlyBlocks);
    }

    public Observable<ApiResponseModel<Game>> createGame(int gameTypeId, @NonNull String token) {
        return ApiFactory.getService().createGame(gameTypeId, token);
    }

    public Observable<ApiResponseModel<Game>> createGameAgainstUser(int userFollowerId, String token) {
        return ApiFactory.getService().createGameAgainstUser(GameType.USER, userFollowerId, token);
    }

    public Observable<ApiResponseModel<List<Game>>> games(int isFinished, int page, int perPage, @NonNull String token) {
        return ApiFactory.getService().games(isFinished, page, perPage, token);
    }

    public Observable<ApiResponseModel<List<Game>>> gamesHistory(int page, int perPage, @Nullable Integer isWinner, @Nullable Integer isLoser, @Nullable Integer isDraw, @NonNull String token) {
        return ApiFactory.getService().gamesHistory(1, page, perPage, isWinner, isLoser, isDraw, token);
    }

    public Observable<ApiResponseModel<Game>> game(int gameId, String token) {
        return ApiFactory.getService().game(gameId, token);
    }

    public Observable<ApiResponseModel<Game>> updateGame(int gameId, int isApproved, String token) {
        return ApiFactory.getService().updateGame(gameId, isApproved, token);
    }

    public Observable<ApiResponseModel<Void>> closeGame(int gameId, String token) {
        return ApiFactory.getService().closeGame(gameId, token);
    }

    public Observable<ApiResponseModel<Game>> createRound(int gameId, int categoryId, @NonNull String token) {
        return ApiFactory.getService().createRound(gameId, categoryId, token);
    }

    public Observable<ApiResponseModel<List<Question>>> questions(int categoryId, @NonNull String token) {
        return ApiFactory.getService().questions(categoryId, token);
    }

    public Observable<ApiResponseModel<Void>> sendQuestion(String name, int categoryId, String answer1, String answer2, String answer3, String answer4, String source, String author, String reference, String rights, String filePath, String token) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody categoryIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(categoryId));
        RequestBody answer1Body = RequestBody.create(MediaType.parse("text/plain"), answer1);
        RequestBody answer2Body = RequestBody.create(MediaType.parse("text/plain"), answer2);
        RequestBody answer3Body = RequestBody.create(MediaType.parse("text/plain"), answer3);
        RequestBody answer4Body = RequestBody.create(MediaType.parse("text/plain"), answer4);
        RequestBody sourceBody = RequestBody.create(MediaType.parse("text/plain"), source);
        RequestBody authorBody = RequestBody.create(MediaType.parse("text/plain"), author);
        RequestBody refBody = RequestBody.create(MediaType.parse("text/plain"), reference);
        RequestBody rightsBody = null;
        if(rights != null)
            rightsBody = RequestBody.create(MediaType.parse("text/plain"), rights);
        MultipartBody.Part fileBody = null;
        if(filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse(""), file);
            fileBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        }
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        return ApiFactory.getService().sendQuestion(nameBody, categoryIdBody, answer1Body, answer2Body, answer3Body, answer4Body, sourceBody, authorBody, refBody, rightsBody, fileBody, tokenBody);
    }


    @Override
    public Observable<ApiResponseModel<List<User>>> friends(@NonNull String token, int page, int perPage, String query) {
        return ApiFactory.getService().users(token, page, perPage, query, 1, 0);
    }

    @Override
    public Observable<ApiResponseModel<List<Gift>>> gifts(@NonNull String token, int page, int perPage) {
        return ApiFactory.getService().gifts(token, page, perPage);
    }

    public Observable<ApiResponseModel<User>> profile(@NonNull String token) {
        return ApiFactory.getService().profile(token);
    }

    public Observable<ApiResponseModel<UsersCount>> usersCount() {
        return ApiFactory.getService().usersCount();
    }

    public Observable<ApiResponseModel<List<User>>> userWithId(int userId, String token) {
        return ApiFactory.getService().userWithId(userId, token);
    }

    public Observable<ApiResponseModel<Void>> addFriend(int userId, @NonNull String token) {
        return ApiFactory.getService().addFriend(userId, token);
    }

    public Observable<ApiResponseModel<Void>> deleteFriend(int userId, String token) {
        return ApiFactory.getService().deleteFriend(userId, token);
    }

    public Observable<ApiResponseModel<Game>> sendAnswer(int roundId, int questionId, String answerId, String token) {
        return ApiFactory.getService().sendAnswer(roundId, questionId, answerId, token);
    }

    public Observable<ApiResponseModel<GameResult>> result(int gameId, int isSurrender, String token) {
        return ApiFactory.getService().result(gameId, isSurrender, token);
    }

    public Observable<ApiResponseModel<Page>> page(String alias) {
        return ApiFactory.getService().page(alias);
    }

    public Observable<ApiResponseModel<List<SocialUser>>> socials(String token, String socialId, String socialToken, String captchaSid, String captchaKey) {
        return ApiFactory.getService().socials(token, socialId, socialToken, captchaSid, captchaKey);
    }

    public Observable<ApiResponseModel<VisibleResults>> visibleResults(String token) {
        return ApiFactory.getService().visibleResults(token);
    }

    public Observable<ApiResponseModel<Void>> block(int userId, String token) {
        return ApiFactory.getService().block(userId, token);
    }

    public Observable<ApiResponseModel<Void>> delBlock(int userId, String token) {
        return ApiFactory.getService().delBlock(userId, token);
    }

    public Observable<ApiResponseModel<Void>> updateProfile(String token, String oldPassword, String newPassword, String genderId, String name, String email, String avatar) {
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody oldPassBody = oldPassword != null ? RequestBody.create(MediaType.parse("text/plain"), oldPassword) : null;
        RequestBody newPassBody = newPassword != null ? RequestBody.create(MediaType.parse("text/plain"), newPassword) : null;
        RequestBody genderIdBody = genderId != null ? RequestBody.create(MediaType.parse("text/plain"), genderId) : null;
        RequestBody nameBody = name != null ? RequestBody.create(MediaType.parse("text/plain"), name) : null;
        RequestBody emailBody = email != null ? RequestBody.create(MediaType.parse("text/plain"), email) : null;
        MultipartBody.Part fileBody = null;
        if(avatar != null) {
            File file = new File(avatar);
            RequestBody requestFile = RequestBody.create(MediaType.parse(""), file);
            fileBody = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        }
        return ApiFactory.getService().updateProfile(tokenBody, oldPassBody, newPassBody, genderIdBody, nameBody, emailBody, fileBody);
    }

    public Observable<ApiResponseModel<User>> orders(String token, int giftId, String email) {
        return ApiFactory.getService().orders(token, giftId, email);
    }

    public Observable<ApiResponseModel<Void>> sendSmile(String token, int gameId, int smileId) {
        return ApiFactory.getService().sendSmile(token, gameId, smileId);
    }

    public Observable<ApiResponseModel<Void>> addToken(String token, String deviceToken) {
        return ApiFactory.getService().addToken(token, deviceToken);
    }

    public Observable<ApiResponseModel<Void>> delToken(String token) {
        return ApiFactory.getService().delToken(token);
    }

}
