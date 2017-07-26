package com.opentmn.opentmn.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.opentmn.opentmn.Config.LOG_TAG;

/**
 * Created by Alexey Antonchik on 16.12.16.
 */

public class FBAuth {

    private Context mContext;
    private AuthInterface mAuthInterface;
    private CallbackManager mCallbackManager;

    public FBAuth(Context context, AuthInterface authInterface, CallbackManager callbackManager) {
        mContext = context;
        mAuthInterface = authInterface;
        mCallbackManager = callbackManager;
    }

    public void initAuth(){

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final String token = loginResult.getAccessToken().getToken();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());

                                        if (object != null) {
                                            try {
                                                String email = object.getString("email");
                                                String id = object.getString("id");
                                                Log.d(LOG_TAG, email + " " + id + " " + token);

                                                mAuthInterface.successAuth(id, token, email);
                                            } catch (JSONException ex) {
                                                Log.d(LOG_TAG, "error auth");
                                            }

                                        } else {
                                            Log.d(LOG_TAG, "error auth");
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(LOG_TAG, "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(LOG_TAG, exception.toString());
                    }
                });
    }



    public interface AuthInterface{
        void successAuth(String id, String token, String email);
    }
}
