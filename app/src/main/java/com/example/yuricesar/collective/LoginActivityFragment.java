package com.example.yuricesar.collective;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yuricesar.collective.data.BD;
import com.example.yuricesar.collective.data.CelulaREST;
import com.example.yuricesar.collective.data.UserInfo;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    private UserInfo user;

    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private BD bd;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            final Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

            final String[] email = new String[1];
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                user = new UserInfo();
                                user.setName(profile.getName());
                                user.setId(profile.getId());
                                user.setPicture(profile.getProfilePictureUri(160, 160).toString());
                                user.setEmail(object.getString("email"));

                                getUserInterest(profile, "movies", user);
                                getUserInterest(profile, "books", user);
                                getUserInterest(profile, "games", user);
                                getUserInterest(profile, "music", user);
                                getUserInterest(profile, "television", user);

                                criarUser();

                                addAmigos();

                                Intent it = new Intent();
                                it.setClass(getActivity(), MainActivity.class);
                                it.putExtra("ID", user.getId());
                                it.putExtra("Nome", user.getName());
                                it.putExtra("Picture", user.getURLPicture());
                                it.putExtra("Email", user.getEmail());
                                try {
                                    startActivity(it);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });



            Bundle parameters = new Bundle();
            parameters.putString("user_friends", "email");
            request.setParameters(parameters);
            request.executeAsync();


        }

        private void criarUser() {
            if (!bd.containsUser(user.getId())) {
                bd.insertUser(user);

                try {
                    new CelulaREST().novoUsuario(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void addAmigos(){
        UserInfo ygor = new UserInfo();
        ygor.setName("Ygor Santos");
        ygor.setId("836885636398591");
        ygor.setPicture("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn2/v/t1.0-1/c254.36.452.452/s50x50/945140_455330777887414_891839605_n.jpg?oh=321c9a6861239b67abd6f7b28f93f9ba&oe=563DDB8D&__gda__=1447440487_9bc4512ae9b4d57d8835fac0a465e845");
        ygor.setEmail("ygor_gs@live.com");
        criarUser(ygor);
    }

    private void criarUser(UserInfo u) {
        if (!bd.containsUser(u.getId())) {
            bd.insertUser(u);

            try {
                new CelulaREST().novoUsuario(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public LoginActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        bd = new BD(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions("user_friends", "email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile){
        if(profile != null){

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void getUserInterest(Profile profile, final String category, final UserInfo user){
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + profile.getId() + "/" + category,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            ArrayList<String> list = new ArrayList<String>();
                            JSONArray array = response.getJSONObject().getJSONArray("data");
                            int qtd = array.length();
                                for (int i = 0; i < qtd; i++) {
                                    list.add(array.getJSONObject(i).get("name").toString());
                                }
                                switch(category){
                                    case "movies":
                                        user.addUserMovies(list);
                                        break;
                                    case "books":
                                        user.addUserBooks(list);
                                        break;
                                    case "games":
                                        user.addUserGames(list);
                                        break;
                                    case "music":
                                        user.addUserMusic(list);
                                        break;
                                    case "television":
                                        user.addUserTv(list);
                                        break;
                                }
                            Log.d("Interests", "add " + category);
                            Log.d("result", list.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("user_friends", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }

}
