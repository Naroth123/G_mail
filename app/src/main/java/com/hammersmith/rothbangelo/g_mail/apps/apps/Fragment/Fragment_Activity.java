package com.hammersmith.rothbangelo.g_mail.apps.apps.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hammersmith.rothbangelo.g_mail.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


import static com.hammersmith.rothbangelo.g_mail.R.id.txtemail;
import static com.hammersmith.rothbangelo.g_mail.R.id.txtpassword;

/**
 * Created by Roth B Angelo on 11/12/2015.
 */
public class Fragment_Activity extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {


    private EditText eName, ePassword;
    private SignInButton btn_g;

    private Button log_o;
    private TextInputLayout inputLayout_email, inputLayout_password;



    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 1;

    private static final String TAG = "Fragment_Activity";
    //Email
    String personName;
    String personEmail;
    Uri pfimg;

    //Facebook
    private ProgressDialog mProgressDialog;
    private LoginButton btn_f;
    CallbackManager callbackManager;
    ProfilePictureView profilePictureView;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;



    String fName,fEmail,fphoto;
    ImageView mImagepro;

    private TextView Name,Link, Email,mName,mEmail;

    public String  fullname, email,link;

    private Profile pendingUpdateUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        buildGoogleApiClient(null);

        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
              profileFb(currentProfile);
//          Profile.setCurrentProfile(currentProfile);
            }

        };
        accessToken = AccessToken.getCurrentAccessToken();
        Profile.fetchProfileForCurrentAccessToken();
        profileFb(Profile.getCurrentProfile());
//        accessTokenTracker.stopTracking();
//        profileTracker.startTracking();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_main,container,false);


        //Edit Text
        eName = (EditText) v.findViewById(txtemail);
        ePassword = (EditText) v.findViewById(txtpassword);

        //Text input layout
        inputLayout_email = (TextInputLayout) v.findViewById(R.id.input_layout_email);
        inputLayout_password = (TextInputLayout) v.findViewById(R.id.input_layout_password);
        //to show info fb
        Name = (TextView) v.findViewById(R.id.name);
        Email = (TextView)v.findViewById(R.id.Email);

        //to show info about Gmail
        mName = (TextView) v.findViewById(R.id.tv_name);
        mEmail = (TextView) v.findViewById(R.id.tv_email);
        mImagepro = (ImageView) v.findViewById(R.id.img_profile);

        profilePictureView = (ProfilePictureView) v.findViewById(R.id.pfpic);
        if(pendingUpdateUser != null){
            profileFb(pendingUpdateUser);
            pendingUpdateUser = null;

        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    accessToken = loginResult.getAccessToken();
                    inputLayout_email.setVisibility(View.GONE);
                    inputLayout_password.setVisibility(View.GONE);
                    btn_g.setVisibility(View.GONE);
                    Name.setVisibility(View.VISIBLE);
                    Email.setVisibility(View.VISIBLE);
                    profilePictureView.setVisibility(View.VISIBLE);
                    RequestData();

//                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        profileFb(profile);
                    }
                }
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
//
//    {END FACEBOOK}
////
//        START GMAIL
        log_o = (Button) v.findViewById(R.id.sign_out);
        log_o.setOnClickListener(this);
        btn_g = (SignInButton) v.findViewById(R.id.sign_in_G);
        btn_g.setOnClickListener(this);


        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_f = (LoginButton) view.findViewById(R.id.sign_in_f);
        btn_f.setOnClickListener(this);
        // {START}Facebook
        btn_f.setReadPermissions(Arrays.asList("public_profile","email"));
        btn_f.setFragment(this);
        RequestData();

            inputLayout_email.setVisibility(View.VISIBLE);
            inputLayout_password.setVisibility(View.VISIBLE);
            btn_g.setVisibility(View.VISIBLE);
            Name.setVisibility(View.GONE);
            Email.setVisibility(View.GONE);
            profilePictureView.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
//        profileFb();
        // AppEventsLogger.deactivateApp(getActivity());
        // profileFb(Profile.getCurrentProfile());
    }

    @Override
    public void onResume() {
        super.onResume();
        //  Profile profile = Profile.getCurrentProfile();

//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();
         AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
         AppEventsLogger.deactivateApp(getActivity());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        }
    }
    private void profileFb(Profile profile) {
        if(Name == null||Email == null|| !isAdded()){

        pendingUpdateUser = profile;
            return;

        }
        if(profile == null){


            profilePictureView.setProfileId(null);
            Name.setText("");
            Email.setText("");
            updateUI(true);

            {
                updateUI(false);

            }

            profilePictureView.setVisibility(View.GONE);
            Name.setVisibility(View.GONE);
            Email.setVisibility(View.GONE);


        }

    }
        //{REQUEST DATA FROM FACEBOOK}
    public void  RequestData(){

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

          JSONObject json = response.getJSONObject();

           if(json != null){
               try {
                   fullname = json.getString("name");
                   email = json.getString("email");
                   link = json.getString("id");


                   Name.setText(fullname);
                   Email.setText(email);
//                   Link.setText(link);
                   profilePictureView.setProfileId(link);
               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onStart() {
        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        }
//        else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }
        //[Build Google]
    private void buildGoogleApiClient(String accountName) {
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile();

        if (accountName != null) {
            gsoBuilder.setAccountName(accountName);
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build());

        mGoogleApiClient = builder.build();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]
    // [START signOut]

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            pfimg = acct.getPhotoUrl();
            Toast.makeText(getActivity(), pfimg +" ",Toast.LENGTH_SHORT).show();
            // set view
            mName.setText(personName);
            mEmail.setText(personEmail);
            Picasso.with(getActivity()).load(pfimg).into(mImagepro);
            Toast.makeText(getActivity(), personName + " " + personEmail, Toast.LENGTH_SHORT).show();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    //{UI}
    private void updateUI(boolean isSignedIn){
        if(isSignedIn){

            inputLayout_email.setVisibility(View.GONE);
            inputLayout_password.setVisibility(View.GONE);
            btn_g.setVisibility(View.GONE);
            eName.setVisibility(View.GONE);
            btn_f.setVisibility(View.GONE);
//            profilePictureView.setVisibility(View.VISIBLE);
            log_o.setVisibility(View.VISIBLE);
            mImagepro.setVisibility(View.VISIBLE);
            ePassword.setVisibility(View.GONE);
            mEmail.setVisibility(View.VISIBLE);
            mName.setVisibility(View.VISIBLE);

        } else{

            inputLayout_email.setVisibility(View.VISIBLE);
            inputLayout_password.setVisibility(View.VISIBLE);
//            profilePictureView.setVisibility(View.GONE);
            btn_g.setVisibility(View.VISIBLE);
            eName.setVisibility(View.VISIBLE);
            ePassword.setVisibility(View.VISIBLE);
            mImagepro.setVisibility(View.GONE);
            btn_f.setVisibility(View.VISIBLE);
            log_o.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
            mName.setVisibility(View.GONE);
        }
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.sign_in_G:
                signIn();
                break;
            case R.id.sign_out:
                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you wnat to sign out?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;


        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Toast.makeText(getActivity(),"Faild connection please try again!",Toast.LENGTH_SHORT).show();

    }
}
