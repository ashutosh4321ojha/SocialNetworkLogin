package com.example.ashutosh.socialnetworklogin.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ashutosh.socialnetworklogin.R;
import com.example.ashutosh.socialnetworklogin.util.CommonData;
import com.example.ashutosh.socialnetworklogin.util.UserInformation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

/*
755ate4d54do5r         linkedin client id
3XW0F2trXNVoMroo       linkedin sercret

 zymRffJeaqGetbZIqrnjjdchF  api twitter
 BKRIIvUGrU9Lhd8ePcH4Uhz9x14c72aT1A0k8kISgHCC6W61jK      api secret twitter

  85e39a0967c342c6a99e95f41251dd5702a49e20        fabric api
  07943359ded879a828d9d646b8f5f9f63a6b53fe38ad6fb702bedf64acc192a9     build secret fabric

* */

public class MainActivity extends AppCompatActivity {


    CallbackManager mFacebookCallbackManager;
    LoginButton mFacebookLoginButton;
    GoogleApiClient mGoogleApiClient;
    SignInButton mGoogleLoginButton;
    Button mLinkedinLoginButton;
    Button mManualLoginButton;
    TwitterLoginButton mTwitterLoginButton;

    String firstName, lastName, gender, email, imageUrl;

    private static final int RC_SIGN_IN = 9001;

    private static final String TWITTER_KEY = "zymRffJeaqGetbZIqrnjjdchF";
    private static final String TWITTER_SECRET = " BKRIIvUGrU9Lhd8ePcH4Uhz9x14c72aT1A0k8kISgHCC6W61jK  ";

    private static final String host = "api.linkedin.com";
    private static final String url = "https://" + host + "/v1/people/~:" +
            "(email-address,firstName,lastName,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new TwitterCore(authConfig));
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_main);

        init();
    }

    //this method is used for linkedin
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void init() {

        mFacebookLoginButton = (LoginButton) findViewById(R.id.facebook_sign_in_button);
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookCallbackRegister();


            }
        });

        mGoogleLoginButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInWithGoogle();
            }
        });

        mLinkedinLoginButton = (Button) findViewById(R.id.linkedin_sign_in_button);
        mLinkedinLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_linkedin();
            }
        });

        mTwitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_sign_in_button);
        mTwitterLoginButton.setCallback(new LoginHandler());

        mManualLoginButton = (Button) findViewById(R.id.manual_sign_in_button);
        mManualLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handleUserData(null, null, null, null, null);
            }
        });

    }

    private class LoginHandler extends Callback<TwitterSession> {


        @Override
        public void failure(TwitterException e) {

            Log.d("Tag", "login fail twitter" + e);
        }

        @Override
        public void success(Result<TwitterSession> result) {

            String name = result.data.getUserName();
            Log.d("Tag", "login success twitter" + name);
            Log.d("Tag", "login success twitter" + result.data.getAuthToken());
            TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
            Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false, new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    Log.d("Tag", "login success name" + result.data.name);
                    Log.d("Tag", "success" + result.data.email);
                    Log.d("Tag", result.data.profileImageUrl);
                    Log.d("Tag", "success" + result.data.name);

                    imageUrl = result.data.profileImageUrl;

                    handleUserData(null, null, null, null, imageUrl);


                }

                @Override
                public void failure(TwitterException e) {

                }
            });


        }
    }

    public void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this,
                buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        Log.d("Tag", "success");
                        Log.d("Tag", "success" + LISessionManager.getInstance(getApplicationContext()).
                                getSession().getAccessToken().toString());

                        getLinkedinUserData();


                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    private void getLinkedinUserData() {


        final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(MainActivity.this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {

                try {

                    getJsonDataLinkedin(apiResponse.getResponseDataAsJson());
                    Log.d("Tag", apiResponse.getResponseDataAsJson().get("firstName").toString());


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Tag", "1" + e);
                }


            }

            @Override
            public void onApiError(LIApiError LIApiError) {

            }
        });
    }

    private void getJsonDataLinkedin(JSONObject responseDataAsJson) {
        try {

            Log.d("TagLinkedin", responseDataAsJson.get("emailAddress").toString());
            Log.d("TagLinkedin", responseDataAsJson.get("firstName").toString());
            Log.d("TagLinkedin", responseDataAsJson.get("lastName").toString());
            Log.d("TagLinkedin", responseDataAsJson.get("pictureUrl").toString());

            email = responseDataAsJson.get("emailAddress").toString();
            firstName = responseDataAsJson.get("firstName").toString();
            lastName = responseDataAsJson.get("lastName").toString();
            imageUrl = responseDataAsJson.get("pictureUrl").toString();

            handleUserData(responseDataAsJson.get("firstName").toString(), lastName, null, email, imageUrl);


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tag", "2" + e);

        }

    }

    //on click of google sign in button
    private void signInWithGoogle() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //on click of facebook login button
    private void facebookCallbackRegister() {

        mFacebookLoginButton.setReadPermissions("user_friends");
        mFacebookLoginButton.setReadPermissions("public_profile");
        mFacebookLoginButton.setReadPermissions("email");
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                Log.d("TAG", "SUCCESS");
                AccessToken accessToken = loginResult.getAccessToken();
                final Profile profile = Profile.getCurrentProfile();


                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.d("Tag", response.toString());
                                try {
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    Log.d("Tag", email);
                                    Log.d("Tag", gender);
                                    Log.d("Tag fb firt name", profile.getFirstName());
                                    //firstName = profile.getFirstName();
                                    //lastName = profile.getLastName();
                                    //imageUrl = profile.getProfilePictureUri(150, 150) + "";

                                    handleUserData(null, null, gender, email, null);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("Tag", "inside catch");


                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,gender");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getGoogleUserData(result);
            Log.d("Tag", "success google" + result.getStatus());
            Log.d("Tag", "success google" + result.isSuccess());

        }
//linked in on activity result action
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);

        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);

    }

    private void getGoogleUserData(GoogleSignInResult result) {

        Log.d("Tag", "handleSignInResult:" + result.isSuccess());
        Log.d("Tag", "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {


            GoogleSignInAccount acct = result.getSignInAccount();
            String name = acct.getDisplayName();
            email = acct.getEmail();
            Log.d("Tag", "success" + name + "" + email + "" + imageUrl);

            handleUserData(null, null, null, email, null);


        } else {

        }

    }

    private void handleUserData(String firstName, String lastName, String gender, String email, String imageUrl) {

        UserInformation userInformation = new UserInformation(firstName, lastName, gender, email, imageUrl);
        CommonData.saveUserInfo(MainActivity.this, userInformation);

        startActivity(new Intent(MainActivity.this, RegistrationForm1.class));

    }
}
